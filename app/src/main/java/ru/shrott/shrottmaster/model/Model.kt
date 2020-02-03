package ru.shrott.shrottmaster.model

import io.reactivex.Observable
import ru.shrott.shrottmaster.model.dto.*

interface Model {

    fun getMasters(): Observable<List<MasterDTO>>
    fun getBadges(idMaster: String): Observable<List<BadgeDTO>>
    fun getNomenclature(nameTemplate: String): Observable<DictionaryDTO>
    fun getPassDetail(barcode: String): Observable<PassDetailDTO>
    fun postPass(passPostDTO: PassPostDTO): Observable<PostPassResponse>
    fun postPhoto(photoPostDTO: PhotoPostDTO): Observable<PostPassResponse>
}