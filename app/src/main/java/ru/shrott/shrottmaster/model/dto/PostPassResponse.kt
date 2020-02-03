package ru.shrott.shrottmaster.model.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PostPassResponse(
    /**
     * Штрих-код
     *
     */
    @SerializedName("barcode") @Expose
    var barcode: String? = null,
    /**
     * Ответ
     *
     */
    @SerializedName("message") @Expose
    var message: String? = null
)