package ru.shrott.shrottmaster.view_model

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.OnLifecycleEvent
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.shrott.shrottmaster.model.PreferenceHelper
import ru.shrott.shrottmaster.model.dto.DictionaryDTO
import ru.shrott.shrottmaster.other.App

class MainViewModel : BaseViewModel(), LifecycleObserver {

    val errorMessage: MutableLiveData<String> = MutableLiveData()

    init {
        App.getComponent()?.inject(this)
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreateView() {
        loadNomenclature()
    }


    fun loadNomenclature() {
        val template = preferenceHelper.getString(PreferenceHelper.TEMPLATE_DB)
        if (template.isNullOrEmpty()) {
            saveDictionary("")
        } else saveDictionary(template)
    }


    private fun saveDictionary(template: String) {
        addDisposable(model.getNomenclature(template)
            .subscribe({ this.onSaveDictionarySuccess(it) }, { this.onFiled(it) }))
    }

    private fun onSaveDictionarySuccess(dictionaryDTO: DictionaryDTO) {

        dictionaryDTO.groups?.let {
            addDisposable(Completable.fromAction {
                db.scrapGroupNomenclature().insertScrapGroupNomenclature(dictionaryDTO.groups) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { preferenceHelper.putString(PreferenceHelper.TEMPLATE_DB, dictionaryDTO.title) })
        }

    }


    private fun onFiled(throwable: Throwable) {
        isLoading.set(false)
        this.errorMessage.value = throwable.message
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}