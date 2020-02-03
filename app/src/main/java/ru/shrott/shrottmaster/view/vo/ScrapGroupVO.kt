package ru.shrott.shrottmaster.view.vo
import android.arch.lifecycle.MutableLiveData
import android.view.View
import ru.shrott.shrottmaster.view.adapters.IBaseItemVm
import ru.shrott.shrottmaster.BR
import ru.shrott.shrottmaster.R

class ScrapGroupVO(var id: String, var label: String, var nomenclatureVOList: List<NomenclatureVO>)  : IBaseItemVm {

    class BackgroundGroup (var selected: Boolean, var full: Boolean)
    var backgroundGroup = BackgroundGroup(false, full = false)
    var clickElement = MutableLiveData<ScrapGroupVO>()

    override val brVariableId: Int
        get() = BR.vmScrapGroup

    override fun getLayoutId(): Int {
        return R.layout.model_scrap_group
    }

    val listener = View.OnClickListener {
        clickElement.value = this
        backgroundGroup.selected = true
    }




}