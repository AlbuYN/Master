package ru.shrott.shrottmaster.model.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ScrapGroupDTO (
    /**
     * Идентификатор
     *
     */
    @SerializedName("id") @Expose val
    id: String,
    /**
     * Наименование группы
     *
     */
    @SerializedName("label") @Expose val
    label: String,
    /**
     * Номенклатура
     *
     */
    @SerializedName("nomenclature") @Expose val
    nomenclature: List<NomenclatureDTO>)