package ru.shrott.shrottmaster.model

import android.content.Context
import android.content.SharedPreferences

class PreferenceHelper(context: Context) {


    var preferences: SharedPreferences = context
            .getSharedPreferences("preferences", Context.MODE_PRIVATE)

    companion object {
        const val TEMPLATE_DB: String = "template_db"
        const val IP_ADDRESS: String = "ip_address"
    }


    fun putString(key: String, value: String) {
        val editor = preferences.edit()
        editor.putString(key, value)
        editor.apply()
    }


    fun getString(key: String): String? {
        return preferences.getString(key, "")
    }
}