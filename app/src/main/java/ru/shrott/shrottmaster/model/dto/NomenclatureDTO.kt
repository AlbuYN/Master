package ru.shrott.shrottmaster.model.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NomenclatureDTO (
    /**
     * Идентификатор
     *
     */
    @SerializedName("id") @Expose val
    id: String,
    /**
     * Наименование номенклатуры
     *
     */
    @SerializedName("label") @Expose val
    label: String)