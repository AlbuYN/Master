package ru.shrott.shrottmaster.model.api

import io.reactivex.Observable
import retrofit2.http.*
import ru.shrott.shrottmaster.model.dto.*


interface ApiInterface {


    @GET
    fun getMasters(@Url url: String): Observable<List<MasterDTO>>
    @GET
    fun getBadges(@Url url: String, @Query("id") id: String): Observable<List<BadgeDTO>>
    @GET
    fun getNomenclature(@Url url: String, @Query("title") nameTemplate: String): Observable<DictionaryDTO>
    @GET
    fun getPassDetail(@Url url: String,@Query("barcode") barcode: String): Observable<PassDetailDTO>
    @POST
    fun postPass(@Url url: String, @Body passPostDTO: PassPostDTO): Observable<PostPassResponse>
    @POST
    fun postPhoto(@Url url: String, @Body photoPostDTO: PhotoPostDTO): Observable<PostPassResponse>

}