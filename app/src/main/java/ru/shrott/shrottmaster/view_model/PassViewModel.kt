package ru.shrott.shrottmaster.view_model

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.OnLifecycleEvent
import android.databinding.ObservableField
import android.net.Uri
import android.os.Bundle
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.shrott.shrottmaster.model.PreferenceHelper
import ru.shrott.shrottmaster.model.dbo.*
import ru.shrott.shrottmaster.model.dto.*
import ru.shrott.shrottmaster.other.App
import ru.shrott.shrottmaster.view.mappers.PassDetailMapper
import ru.shrott.shrottmaster.view.mappers.ScrapGroupNomenclatureMapper
import ru.shrott.shrottmaster.view.vo.*
import javax.inject.Inject




class PassViewModel(val barcode: String?) : BaseViewModel(), LifecycleObserver {


    @Inject
    lateinit var passDetailMapper: PassDetailMapper

    var scrapGroupVOList = MutableLiveData<List<ScrapGroupVO>>()
    var nomenclatureVOList = MutableLiveData<List<NomenclatureVO>>()
    var photoList = MutableLiveData<List<MediaPassVO>>()
    val weightPercentageVOList = MutableLiveData<List<WeightPercentageVO>>()
    var totalPercentPollution = ObservableField(0)
    var totalPercentWeight = MutableLiveData<Int>()//.apply { postValue(0) }
    var passDetailVO = ObservableField<PassDetailVO>()
    val errorMessage: MutableLiveData<String> = MutableLiveData()
    var result = MutableLiveData<Boolean>()
    private var fileUri: Uri? = null // file url to store image

    init {
        App.getComponent()?.inject(this)
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onCreateView() {
        if (scrapGroupVOList.value.isNullOrEmpty()) {
            loadScrapGroup()
        }
        barcode?.let { getPassDetails(it) }
    }

    private fun loadWeightPercentage(barcode: String) {
        addDisposable(db.weightPercentageDao().getByIdPass(barcode)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { onLoadWeightPercentageSuccess(it) })
    }

    private fun loadPhotosFromDB(barcode: String) {
        addDisposable(db.mediaPassDao().getByIdPass(barcode)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { onLoadPhotosSuccess(barcode, it) })
    }

    private fun loadTotalPercentPollution(barcode: String) {
        addDisposable(db.totalPercentPollutionDao().getByIdPass(barcode)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { onLoadTotalPercentPollution(it) })
    }

    private fun onLoadTotalPercentPollution(totalPercentPollutionDBO: TotalPercentPollutionDBO) {
        totalPercentPollution.set(totalPercentPollutionDBO.totalPercentPollution)
    }


    private fun onLoadPhotosSuccess(idPass: String, mediaPassDBOList: List<MediaPassDBO>) {
        photoList.value = mediaPassDBOList.map { mediaPassDBO ->
            MediaPassVO(idPass, mediaPassDBO.uri, mediaPassDBO.imgBase64) }
    }


    private fun loadScrapGroup() {
        barcode?.let {
        addDisposable(db.scrapGroupDao().getAll()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({scrapGroupList -> onScrapGroupSuccess(scrapGroupList, barcode)},
                {error -> onFiled(error)}))
        }
    }

    private fun onLoadWeightPercentageSuccess(weightPercentageDBOList: List<WeightPercentageDBO>) {

        weightPercentageDBOList.forEach {
            val scrapGroupVO = scrapGroupVOList.value?.find { scrapGroupVO ->
                scrapGroupVO.id == it.idScrapGroup }
            scrapGroupVO?.backgroundGroup?.full = true

            val nomenclatureVO =  scrapGroupVO?.nomenclatureVOList?.
                find { nomenclatureVO -> nomenclatureVO.id == it.idNomenclature }
            nomenclatureVO?.full = true
            nomenclatureVO?.percentWeight = it.percentage
        }

        weightPercentageVOList.value = weightPercentageDBOList.map { weightPercentageDBO ->
            WeightPercentageVO(weightPercentageDBO.idNomenclature, weightPercentageDBO.idScrapGroup,
                weightPercentageDBO.name, weightPercentageDBO.percentage)
        }

        var percent = 0

        weightPercentageVOList.value?.forEach { percent += it.percent }
        totalPercentWeight.postValue(percent)
    }

    private fun onScrapGroupSuccess(scrapGroupNomenclatureDBOList: List<ScrapGroupNomenclatureDBO>, barcode: String) {

        if (!scrapGroupNomenclatureDBOList.isNullOrEmpty()) {

            val scrapListTransform = scrapGroupNomenclatureDBOList
                .map { ScrapGroupNomenclatureMapper.map(it, this) }
            scrapListTransform[0].backgroundGroup.selected = true
            scrapGroupVOList.value = scrapListTransform
            nomenclatureVOList.value = scrapListTransform[0].nomenclatureVOList
            subscribeToOnclickElement(scrapListTransform)
            loadWeightPercentage(barcode)
            loadPhotosFromDB(barcode)
            loadTotalPercentPollution(barcode)
        } else {
            loadNomenclature()
        }
    }


    private fun loadNomenclature() {
        val template = preferenceHelper.getString(PreferenceHelper.TEMPLATE_DB)
        if (template.isNullOrEmpty()) {
            addDisposable(model.getNomenclature("")
                .subscribe({ this.onSaveDictionarySuccess(it) }, { this.onFiled(it) }))
        }
    }

    private fun onSaveDictionarySuccess(dictionaryDTO: DictionaryDTO) {

        dictionaryDTO.groups?.let {
            addDisposable(Completable.fromAction {
                db.scrapGroupNomenclature().insertScrapGroupNomenclature(dictionaryDTO.groups) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    preferenceHelper.putString(PreferenceHelper.TEMPLATE_DB, dictionaryDTO.title)
                    loadScrapGroup()
                })
        }

    }

    private fun subscribeToOnclickElement(scrapGroupVOList: List<ScrapGroupVO>) {
        scrapGroupVOList.forEach { scrapGroupVO: ScrapGroupVO ->
            scrapGroupVO.clickElement.observeForever { group -> group?.let {
                nomenclatureVOList.value = group.nomenclatureVOList
                scrapGroupVOList.filterNot { it.id == group.id }.forEach { it.backgroundGroup.selected = false }
            }
            }
        }
    }


    fun addPhoto(barcode: String) {
        createMediaSignal(barcode)?.let {
            addDisposable(Completable.fromAction {
                db.mediaPassDao().insert(MediaPassDBO(it.uri, it.idPass, it.imgBase64))
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { insertBadgedStatus(barcode) })
        }
    }

    fun addWeightPercent(args: Bundle?, weightPercentageVO: WeightPercentageVO) {

        val idPass = args?.getString("barcode")
        if (idPass != null) {

            when (scrapGroupVOList.value?.find { it.id == weightPercentageVO.idScrapGroup }?.
                nomenclatureVOList?.find { it.id == weightPercentageVO.idNomenclature }?.full) {
                true -> updateWeightPercent(idPass, weightPercentageVO)
                false -> insertWeightPercent(idPass, weightPercentageVO)
            }
        }
    }

    fun addTotalPercentPollution(idPass: String, value: Int) {
        if (totalPercentPollution.get()!! > 0) {
            updateTotalPercentPollution(TotalPercentPollutionDBO(idPass, value))
        } else insertTotalPercentPollution(TotalPercentPollutionDBO(idPass, value))
    }


    private fun insertTotalPercentPollution(totalPercentPollutionDBO: TotalPercentPollutionDBO) {
        addDisposable(Completable.fromAction {
            db.totalPercentPollutionDao().insert(totalPercentPollutionDBO)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { insertBadgedStatus(totalPercentPollutionDBO.idPass) })
    }

    private fun updateTotalPercentPollution(totalPercentPollutionDBO: TotalPercentPollutionDBO) {
        addDisposable(Completable.fromAction {
            db.totalPercentPollutionDao().update(totalPercentPollutionDBO)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { })
    }


    private fun insertWeightPercent(barcode: String, weightPercentageVO: WeightPercentageVO) {
        addDisposable(Completable.fromAction {
            db.weightPercentageDao().insert(
                WeightPercentageDBO(
                    weightPercentageVO.idNomenclature, barcode,
                    weightPercentageVO.idScrapGroup, weightPercentageVO.name, weightPercentageVO.percent)) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { insertBadgedStatus(barcode) })
    }


    private fun updateWeightPercent(barcode: String, weightPercentageVO: WeightPercentageVO) {
        addDisposable(Completable.fromAction {
            db.weightPercentageDao().update(WeightPercentageDBO(weightPercentageVO.idNomenclature, barcode,
                weightPercentageVO.idScrapGroup, weightPercentageVO.name, weightPercentageVO.percent))}
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { })
    }


    private fun insertBadgedStatus(barcode: String) {
        addDisposable(Completable.fromAction {
            db.badgeStatusDao().insert(BadgeStatusDBO(barcode)) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { })
    }


    fun createFileUri(): Uri? {
        fileUri = workWithPhotos.newFileUri()
        return fileUri
    }


    private fun createMediaSignal(idPass: String): MediaPassVO? {
        return if (fileUri != null) {
            val bitmap = workWithPhotos.getBitmapFromUri(fileUri!!)
            if (bitmap != null) {
                MediaPassVO(idPass, fileUri.toString(), workWithPhotos.imageToBase64(bitmap))
            } else null

        } else null
    }


    private fun getPassDetails(barCode: String) {
        isLoading.set(true)
        addDisposable(model.getPassDetail(barCode)
            .map(passDetailMapper)
            .subscribe({ this.onPassDetailSuccess(it) }, { this.onFiled(it) }))
    }

    private fun onPassDetailSuccess(passDetailVO: PassDetailVO) {
        isLoading.set(false)
        this.passDetailVO.set(passDetailVO)
    }

    private fun onFiled(throwable: Throwable) {
        isLoading.set(false)
        errorMessage.postValue(errorProcessing.onError(throwable))
    }


    fun sendPass(idMaster: String) {
        isLoading.set(true)
        addDisposable(model.postPass(PassPostDTO(barcode, idMaster,
            weightPercentageVOList.value?.map { ScrapDTO(it.idNomenclature, it.percent) }, totalPercentPollution.get(),
            photoList.value?.map { it.imgBase64 }))
            .subscribe(
                {
                    removeFromDB()
                    removeFromDBPhoto()
                    isLoading.set(false)
                    result.value = true
                },
                {error -> onFiled(error)}))
    }

    private fun removeFromDB() {
        addDisposable(Completable.fromAction {
            barcode?.let { db.weightPercentageDao().deleteByIdPass(it) }
            barcode?.let { db.badgeStatusDao().deleteByIdPass(it) }
            barcode?.let { db.totalPercentPollutionDao().deleteByIdPass(it) }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { })
    }

    private fun removeFromDBPhoto() {
        addDisposable(Completable.fromAction {
        barcode?.let { db.mediaPassDao().deleteByIdPass(it) }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { })
    }



    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

}