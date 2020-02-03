package ru.shrott.shrottmaster.view_model

import android.arch.lifecycle.ViewModel
import ru.shrott.shrottmaster.other.Utils.SingleLiveEvent
import ru.shrott.shrottmaster.view.vo.WeightPercentageVO

class SharedViewModel: ViewModel() {

    val weightPercent = SingleLiveEvent<WeightPercentageVO>()
    val totalPercentPollution = SingleLiveEvent<Int>()
    val updateMasters = SingleLiveEvent<Boolean>()
    val updateBadges = SingleLiveEvent<Boolean>()



}