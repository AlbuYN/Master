package ru.shrott.shrottmaster.model.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PhotoPostDTO(
    /**
     * Штрих-код
     *
     */
    @SerializedName("barcode") @Expose
    var barcode: String? = null,
    /**
     * Список фотографий base64
     *
     */
    @SerializedName("list_photos") @Expose
    var listPhotos: List<String>? = null

)