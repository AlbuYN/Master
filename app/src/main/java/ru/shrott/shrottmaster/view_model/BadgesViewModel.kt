package ru.shrott.shrottmaster.view_model

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.OnLifecycleEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import ru.shrott.shrottmaster.other.App
import ru.shrott.shrottmaster.view.mappers.BadgesMapper
import ru.shrott.shrottmaster.view.vo.BadgeVO
import java.util.*
import javax.inject.Inject

class BadgesViewModel(private val idMaster: String?) : BaseViewModel(), LifecycleObserver {

    @Inject lateinit var badgesMapper: BadgesMapper
    var badgesList = MutableLiveData<List<BadgeVO>>()
    val errorMessage: MutableLiveData<String> = MutableLiveData()

    init {
        App.getComponent()?.inject(this)
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onCreateView() {
        if (isEmptyBadgesList()) {
            idMaster?.let { loadBadges() }
        }
    }


    private fun loadBadgesStats() {
        addDisposable(db.badgeStatusDao().getAll()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                it.forEach { badgesList.value?.find { badgeVO -> badgeVO.barcode.equals(it.idPass) }?.
                    status = BadgeVO.STATUS_FILLED }
                badgesList.value = badgesList.value
            })
    }

    fun loadBadges() {
        if (!idMaster.isNullOrEmpty()) {
            badgesMapper.idMaster = idMaster
            isLoading.set(true)
            addDisposable(model.getBadges(idMaster)
                    .map(badgesMapper)
                    .subscribe({ badges -> onLoadBadgesSuccess(badges) },
                        { error -> onFiled(error) })
            )
        }
    }

    private fun isEmptyBadgesList(): Boolean {
        return badgesList.value.isNullOrEmpty()
    }

    private fun onLoadBadgesSuccess(badgesList: List<BadgeVO>) {
        isLoading.set(false)
        this.badgesList.value = badgesList
        loadBadgesStats()
    }

    fun filter(str: String) : List<BadgeVO>? {
        if (str.isEmpty()) badgesList.value = badgesList.value
        return badgesList.value?.filter { badgeVO ->  badgeVO.barcode
            ?.toLowerCase(Locale.ROOT)!!.contains(str.toLowerCase(Locale.ROOT))}
    }

    private fun onFiled(throwable: Throwable) {
        isLoading.set(false)
        errorMessage.postValue(errorProcessing.onError(throwable))
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }


}