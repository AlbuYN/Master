package ru.shrott.shrottmaster.model.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PassPostDTO (

    /**
     * Штрих-код
     *
     */
    @SerializedName("barcode") @Expose
    var barcode: String? = null,

    /**
     * Идентификатор мастера
     *
     */
    @SerializedName("master_id") @Expose
    var masterId: String? = null,

    /**
     * Процент веса списком
     *
     */
    @SerializedName("list_scrap") @Expose
    var listScrap: List<ScrapDTO>? = null,

    /**
     * Процент засора
     *
     */
    @SerializedName("total_percent") @Expose
    var totalPercent: Int? = null,

    /**
     * Список фотографий base64
     *
     */
    @SerializedName("list_photos") @Expose
    var listPhotos: List<String>? = null
    )

