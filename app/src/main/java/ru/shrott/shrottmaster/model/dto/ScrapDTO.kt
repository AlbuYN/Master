package ru.shrott.shrottmaster.model.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ScrapDTO (
    /**
     * Идентификатор
     *
     */
    @SerializedName("id") @Expose
    var barcode: String? = null,
    /**
     * Процент
     *
     */
    @SerializedName("percent") @Expose
    var percent: Int? = null
)