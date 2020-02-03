package ru.shrott.shrottmaster.view_model

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.OnLifecycleEvent
import android.databinding.ObservableField
import com.google.firebase.messaging.FirebaseMessaging
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.shrott.shrottmaster.model.PreferenceHelper
import ru.shrott.shrottmaster.model.dto.DictionaryDTO
import ru.shrott.shrottmaster.other.App
import ru.shrott.shrottmaster.other.Utils.SingleLiveEvent

class SettingsViewModel : BaseViewModel(), LifecycleObserver {

    var ipAddress = ObservableField<String>()
    var saveIpAddress = SingleLiveEvent<Boolean>()
    val updateNomenclature = SingleLiveEvent<Boolean>()
    val errorMessage: MutableLiveData<String> = MutableLiveData()

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onCreateView() {
        ipAddress.set(preferenceHelper.getString(PreferenceHelper.IP_ADDRESS)?.replace("http://", ""))
    }

    init {
        App.getComponent()?.inject(this)
    }

    fun saveIpAddress() {
        if (!ipAddress.get().isNullOrEmpty()) {
            ipAddress.get()?.let {

                try {
                    val currentTopic = preferenceHelper.getString(PreferenceHelper.IP_ADDRESS)?.replace("http://", "")
                    if (currentTopic != null && currentTopic.isNotEmpty()) {
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(currentTopic)
                    }

                    FirebaseMessaging.getInstance().subscribeToTopic(it)
                    preferenceHelper.putString(PreferenceHelper.IP_ADDRESS, "http://$it")
                    saveIpAddress.postValue(true)
                } catch (e: Exception) {
                    errorMessage.postValue("Подписка недоступна")
                }

            }

            //нужно сделать подписку на FCM
        } else errorMessage.postValue("Необходимо ввести IP-адрес!")
    }

    fun updateNomenclature() {
        preferenceHelper.putString(PreferenceHelper.TEMPLATE_DB, "")
        refreshDictionary()
    }

    private fun refreshDictionary() {
        isLoading.set(true)
        addDisposable(model.getNomenclature("")
            .subscribe({ this.onSaveDictionarySuccess(it) }, { this.onFiled(it) }))
    }

    private fun onSaveDictionarySuccess(dictionaryDTO: DictionaryDTO) {

        dictionaryDTO.groups?.let {
            addDisposable(Completable.fromAction {
                db.scrapGroupNomenclature().insertScrapGroupNomenclature(dictionaryDTO.groups) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    preferenceHelper.putString(PreferenceHelper.TEMPLATE_DB, dictionaryDTO.title)
                    updateNomenclature.postValue(true)
                    isLoading.set(false)
                })
        }

    }

    private fun onFiled(throwable: Throwable) {
        isLoading.set(false)
        this.errorMessage.value = throwable.message
        isLoading.set(false)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }


}