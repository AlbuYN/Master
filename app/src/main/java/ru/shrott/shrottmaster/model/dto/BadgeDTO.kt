package ru.shrott.shrottmaster.model.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



class BadgeDTO (
        /**
         * Наименование контрагента
         *
         */
        @SerializedName("name") @Expose val name: String? = null,
        /**
         * Регистрационный номер автомобиля
         *
         */
        @SerializedName("reg_num") @Expose val regNum: String? = null,
        /**
         * Марка/модель автомобиля
         *
         */
        @SerializedName("model_car") @Expose val modelCar: String? = null,
        /**
         * Штрихкод
         *
         */
        @SerializedName("barcode") @Expose val barcode: String? = null,
        /**
         * 0 - Частично заполне, 1 - новое
         *
         */
        @SerializedName("status") @Expose val status: Int? = null)