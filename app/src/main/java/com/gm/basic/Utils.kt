package com.gm.basic

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Author     : Gowtham
 * Email      : goutham.gm11@gmail.com
 * Github     : https://github.com/goutham106
 * Created on : 2020-01-02.
 */
object Utils {

    private fun generateRetrofit(): Retrofit {
        return Retrofit.Builder()
            .client(OkHttpClient().newBuilder().build())
            .baseUrl("https://androidwave.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }


    fun getApiService(): Api {
        return generateRetrofit().create(Api::class.java)
    }


}