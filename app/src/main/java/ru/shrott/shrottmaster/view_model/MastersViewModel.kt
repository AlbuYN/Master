package ru.shrott.shrottmaster.view_model

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.OnLifecycleEvent
import ru.shrott.shrottmaster.model.dto.MasterDTO
import ru.shrott.shrottmaster.other.App
import ru.shrott.shrottmaster.other.Utils.SingleLiveEvent
import ru.shrott.shrottmaster.view.vo.MasterVO

class MastersViewModel : BaseViewModel(), LifecycleObserver {


    var mastersList = MutableLiveData<List<MasterVO>>()
    val errorMessage = SingleLiveEvent<String>()

    init {
        App.getComponent()?.inject(this)
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onCreateView() {
        if (mastersList.value.isNullOrEmpty()) {
            loadMastersList()
        }
    }

    fun loadMastersList() {
        isLoading.set(true)
        addDisposable(model.getMasters()
            .flatMapIterable { it }
            .map { masterDTO: MasterDTO -> MasterVO(masterDTO.id, masterDTO.name) }
            .toList()
            .subscribe({ masters -> onLoadMastersSuccess(masters) },
                { error -> onFiled(error)})
        )
    }

    private fun onLoadMastersSuccess(masters: List<MasterVO>) {
        isLoading.set(false)
        this.mastersList.value = masters

    }

    private fun onFiled(throwable: Throwable) {
        isLoading.set(false)
        mastersList.value = ArrayList()
        errorMessage.postValue(errorProcessing.onError(throwable))
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}