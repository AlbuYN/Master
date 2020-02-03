package ru.shrott.shrottmaster.model.api

import android.content.Context
import org.json.JSONObject
import retrofit2.HttpException
import ru.shrott.shrottmaster.R
import javax.inject.Inject

class ErrorProcessing @Inject constructor(var applicationContext: Context) {


    fun onError(throwable: Throwable): String {

        return when {
            throwable.message?.contains("500", true)!! -> throwable.message.toString()
            throwable is HttpException -> {
                val str = throwable.response()?.errorBody()?.string()
                val post = JSONObject(str)
                post.getString("message")

            }
            throwable.message?.contains("Failed to connect", true)!! ->
                applicationContext.getString(R.string.network_unavailable)
            else -> throwable.message!!
        }
    }

}