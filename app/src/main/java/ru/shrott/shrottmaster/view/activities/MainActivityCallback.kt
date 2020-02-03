package ru.shrott.shrottmaster.view.activities

import android.os.Bundle


interface MainActivityCallback {

    fun openTotalPercentFragment(args: Bundle)
    fun openSettings()
    fun hideKeyboard()
    fun loadNomenclature()
}