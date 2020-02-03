package ru.shrott.shrottmaster.model

import io.reactivex.Observable
import io.reactivex.Scheduler
import ru.shrott.shrottmaster.model.api.ApiInterface
import ru.shrott.shrottmaster.model.dto.*
import ru.shrott.shrottmaster.other.App
import ru.shrott.shrottmaster.other.Const
import javax.inject.Inject
import javax.inject.Named

class ModelImpl : Model {

    @Inject
    lateinit var apiInterface: ApiInterface

    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    @Inject
    @field:Named(Const.IO_THREAD)
    lateinit var ioThread: Scheduler


    @Inject
    @field:Named(Const.UI_THREAD)
    lateinit var uiThread: Scheduler



    init {
        App.getComponent()?.inject(this)
    }

    private fun <T> Observable<T>.applySchedulers(): Observable<T> =
        this.subscribeOn(ioThread)
            .observeOn(uiThread)
            .unsubscribeOn(ioThread)


    private fun getBaseUrl(): String {
        val baseUrl = preferenceHelper.getString(PreferenceHelper.IP_ADDRESS)
        return if (!baseUrl.isNullOrEmpty()) baseUrl else Const.BASE_URL
    }


    override fun getMasters(): Observable<List<MasterDTO>> {
        return apiInterface.getMasters("${getBaseUrl()}/mas/hs/MasterMobile?action=getusers").applySchedulers()
        /*
        val listMasterDTOs = mutableListOf<MasterDTO>()

        var masterDTO: MasterDTO

        for (i in 1..5) {
            masterDTO = MasterDTO(i.toLong(), "Мастер $i")
            listMasterDTOs.add(masterDTO)
        }

        return create { subscriber ->
            subscriber.onNext(listMasterDTOs)
            subscriber.onCompleted()
        }
        */
    }

    override fun getBadges(idMaster: String): Observable<List<BadgeDTO>> {
        return apiInterface.getBadges("${getBaseUrl()}/mas/hs/MasterMobile?action=getobjects", idMaster)

            .applySchedulers()


        /*
        val listBadgesDTOs = mutableListOf<BadgeDTO>()

        var badgeDTO: BadgeDTO

        for (i: Int in 1..5) {
            badgeDTO = BadgeDTO(i.toLong(), "Попов", "о282оа11", "Volkswagen", "$i 434324324325322", BadgeVO.STATUS_NEW)
            listBadgesDTOs.add(badgeDTO)
        }

        listBadgesDTOs.add(BadgeDTO(6, "Иванов", "о359оа11", "Audi", "$4010000000067", BadgeVO.STATUS_NEW))

        return create { subscriber ->
            subscriber.onNext(listBadgesDTOs)
            subscriber.onCompleted()
        }
        */
    }


    override fun getNomenclature(nameTemplate: String): Observable<DictionaryDTO> {
        return apiInterface.getNomenclature("${getBaseUrl()}/mas/hs/MasterMobile?action=getnomenclature", nameTemplate)
            .applySchedulers()

        /*
        val listGroup = mutableListOf<ScrapGroupDTO>()

        var idNomenclature = 1

        for (i: Int in 1..5) {
            val listNomenclature = mutableListOf<NomenclatureDTO>()
            for (n: Int in 1..4) {
                val nomenclatureDTO = NomenclatureDTO(idNomenclature.toLong(), "Лом стальной ЛС$i")
                listNomenclature.add(nomenclatureDTO)
                idNomenclature += 1
            }

            val scrapGroupDTO = ScrapGroupDTO(i.toLong(), "ЛС$i", listNomenclature)
            listGroup.add(scrapGroupDTO)
        }

        return create { subscriber ->
            subscriber.onNext(DictionaryDTO("t1", listGroup))
            subscriber.onCompleted()
        }
        */
    }

    override fun getPassDetail(barcode: String): Observable<PassDetailDTO> {
        return apiInterface.getPassDetail("${getBaseUrl()}/mas/hs/MasterMobile?action=getdoc", barcode).applySchedulers()

        //return just(PassDetailDTO("Васильев А.А", "о034тк", "Ауди 100",
          //  "1234567890", 200, 100, 123, "test"))
    }

    override fun postPass(passPostDTO: PassPostDTO): Observable<PostPassResponse> {
        return apiInterface.postPass("${getBaseUrl()}/mas/hs/MasterMobile?action=postdata", passPostDTO).applySchedulers()
    }

    override fun postPhoto(photoPostDTO: PhotoPostDTO): Observable<PostPassResponse> {
        return apiInterface.postPhoto("${getBaseUrl()}/mas/hs/MasterMobile?action=postimage", photoPostDTO).applySchedulers()
    }
}