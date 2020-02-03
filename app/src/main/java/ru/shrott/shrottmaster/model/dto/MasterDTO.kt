package ru.shrott.shrottmaster.model.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MasterDTO (
    /**
     * Идентификатор
     *
     */
    @SerializedName("id") @Expose var id: String,
    /**
     * ФИО мастера
     *
     */
    @SerializedName("name") @Expose var name: String? = null)