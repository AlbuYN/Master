package ru.shrott.shrottmaster.view.vo

import android.os.Bundle
import androidx.navigation.Navigation
import ru.shrott.shrottmaster.view.adapters.IBaseItemVm
import ru.shrott.shrottmaster.BR
import ru.shrott.shrottmaster.R

class MasterVO(var id: String, var name: String?) : IBaseItemVm {

    private val bundle = Bundle()

    init {
        bundle.putString("idMaster", id)
        bundle.putString("name", name)
    }

    override val brVariableId: Int
        get() = BR.vmMaster

    override fun getLayoutId(): Int {
        return R.layout.model_master
    }

    val listener = Navigation
            .createNavigateOnClickListener(R.id.action_mastersFragment_to_badgeListFragment, bundle)

}