package ru.shrott.shrottmaster.view_model

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import ru.shrott.shrottmaster.model.Model
import ru.shrott.shrottmaster.model.PreferenceHelper
import ru.shrott.shrottmaster.model.api.ErrorProcessing
import ru.shrott.shrottmaster.model.database.AppDatabase
import ru.shrott.shrottmaster.other.Utils.Utils
import ru.shrott.shrottmaster.other.Utils.WorkWithPhotos

import javax.inject.Inject

abstract class BaseViewModel : ViewModel() {

    @Inject
    lateinit var model: Model

    @Inject
    lateinit var compositeDisposable: CompositeDisposable

    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    @Inject
    lateinit var db: AppDatabase

    @Inject
    lateinit var errorProcessing: ErrorProcessing

    @Inject
    lateinit var utils: Utils

    @Inject
    lateinit var workWithPhotos: WorkWithPhotos

    var isLoading = ObservableField(false)


    fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

}