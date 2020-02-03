package ru.shrott.shrottmaster.view.vo

import android.os.Bundle
import androidx.navigation.Navigation
import ru.shrott.shrottmaster.view.adapters.IBaseItemVm
import ru.shrott.shrottmaster.BR
import ru.shrott.shrottmaster.R

class BadgeVO(var name: String?, var regNum: String?, var modelCar: String?,
              var barcode: String?, var status: Int?, var idMaster: String?) : IBaseItemVm {

    companion object {
        const val STATUS_NEW = 0
        const val STATUS_FILLED = 1
    }

    private val bundle = Bundle()

    init {
        bundle.putString("barcode", barcode)
        bundle.putString("idMaster", idMaster)
    }

    override val brVariableId: Int
        get() = BR.vmBadge

    override fun getLayoutId(): Int {
        return R.layout.model_badge
    }



    val listener = Navigation
        .createNavigateOnClickListener(R.id.passFragment, bundle)


}