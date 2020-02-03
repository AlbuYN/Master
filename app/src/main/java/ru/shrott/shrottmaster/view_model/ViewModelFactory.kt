package ru.shrott.shrottmaster.view_model

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider


@Suppress("UNCHECKED_CAST")
class ViewModelFactory (val id: String?) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(BadgesViewModel::class.java) -> BadgesViewModel(id) as T
            modelClass.isAssignableFrom(PassViewModel::class.java) -> PassViewModel(id) as T
            else -> throw IllegalArgumentException("ViewModel Not Found")
        }
    }

}