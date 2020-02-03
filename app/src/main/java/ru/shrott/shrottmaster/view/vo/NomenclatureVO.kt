package ru.shrott.shrottmaster.view.vo

import android.arch.lifecycle.ViewModel
import android.os.Bundle
import androidx.navigation.Navigation
import ru.shrott.shrottmaster.view.adapters.IBaseItemVm
import ru.shrott.shrottmaster.BR
import ru.shrott.shrottmaster.R
import ru.shrott.shrottmaster.view_model.PassViewModel


class NomenclatureVO(var id: String, var idScrapGroup: String, var label: String,
                     passViewModel: PassViewModel, var full: Boolean, var percentWeight: Int = 0) : IBaseItemVm,
    ViewModel() {

    private var bundle = Bundle()

    init {
        bundle.putString("idNomenclature", id)
        bundle.putString("name", label)
        bundle.putString("idScrapGroup", idScrapGroup)

        passViewModel.totalPercentWeight.observeForever { it?.let { it1 -> bundle.putInt("totalPercentWeight", it1 - percentWeight) } }

    }



    override fun getLayoutId(): Int {
        return R.layout.model_nomenclature
    }

    override val brVariableId: Int
        get() = BR.vmNomenclature



    val listener = Navigation.createNavigateOnClickListener(R.id.action_passFragment_to_selectScrapCalculateFragment, bundle)



}