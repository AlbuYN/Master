package ru.shrott.shrottmaster.view.vo

import ru.shrott.shrottmaster.view.adapters.IBaseItemVm
import ru.shrott.shrottmaster.BR
import ru.shrott.shrottmaster.R

class WeightPercentageVO(var idNomenclature: String, var idScrapGroup: String,
                         var name: String, var percent: Int): IBaseItemVm {
    override val brVariableId: Int
        get() = BR.vmNomenclaturePercent

    override fun getLayoutId(): Int {
        return R.layout.model_nomenclature_percent
    }
}