package ru.shrott.shrottmaster.model.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DictionaryDTO(
    /**
     * Наименование шаблона
     *
     */
    @SerializedName("title") @Expose val
    title: String,
    /**
     * Группы номенклатуры
     *
     */
    @SerializedName("groups") @Expose val
    groups: List<ScrapGroupDTO>? = null)