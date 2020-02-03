package ru.shrott.shrottmaster.model.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PassDetailDTO(
    /**
     * Имя мастера
     *
     */
    @SerializedName("name") @Expose var name: String?,
    /**
     * Регистрационный номер машины
     *
     */
    @SerializedName("reg_num") @Expose var regNum: String?,
    /**
     * Модель машины
     *
     */
    @SerializedName("model_car") @Expose var modelCar: String?,
    /**
     * Штрихкод
     *
     */
    @SerializedName("barcode") @Expose var barcode: String,
    /**
     * Вес авто по птс
     *
     */
    @SerializedName("auto_weigth_pts") @Expose var autoWeightPts: Int?,
    /**
     * Вес авто после взвешивания
     *
     */
    @SerializedName("first_weight") @Expose var firstWeight: Int?,
    /**
     * Номер взвешивания, пока не нужно
     *
     */
    @SerializedName("pass_num") @Expose var passNum: Int?,
    /**
     * Строка с доп информацией, пока не нужно
     *
     */
    @SerializedName("info") @Expose var info: String?) {
}