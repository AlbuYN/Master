package ru.shrott.shrottmaster.model.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


private var httpClient = OkHttpClient()

class ApiFactory {

    companion object {
        fun getApiInterface(apiEndPoint: String): ApiInterface {
            return createApiInterface(apiEndPoint)
        }

        private fun createApiInterface(apiEndpoint: String): ApiInterface {

            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            httpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

            return getRetrofit(httpClient, apiEndpoint).create(ApiInterface::class.java)

        }

        private fun getRetrofit(httpClient: OkHttpClient, apiEndpoint: String): Retrofit {
            return Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(apiEndpoint)
                .client(httpClient)
                .build()
        }
    }
}