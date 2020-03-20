package com.gm.basic.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.util.Log
import com.gm.basic.domain.Api
import com.gm.basic.room.AppDatabase
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Author     : Gowtham
 * Email      : goutham.gm11@gmail.com
 * Github     : https://github.com/goutham106
 * Created on : 2020-01-02.
 */
object Utils {

    private fun generateRetrofit(context: Context): Retrofit {
        return Retrofit.Builder()
            .client(generateOkHttpClient(context))
            .baseUrl("https://testapi.io/api/crosscodedj/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    private fun generatePagingRetrofit(context: Context): Retrofit {
        return Retrofit.Builder()
            .client(generateOkHttpClient(context))
            .baseUrl("https://testapi.io/api/crosscodedj/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }


    fun getApiService(context: Context): Api {
        return generateRetrofit(context).create(Api::class.java)
    }

    fun getDBService(context: Context): AppDatabase {
        return AppDatabase(context)
    }


    private fun generateOkHttpClient(context: Context): OkHttpClient {
        val cacheSize = (5 * 1024 * 1024).toLong() // 5MB

        val CACHE_CONTROL_INTERCEPTOR: Interceptor = Interceptor { chain ->
            var originalResponse = chain.proceed(chain.request())
            if (hasNetwork(context)) {
                val maxAge = 60 // read from cache for 1 minute
                originalResponse.newBuilder()
                    .header("Cache-Control", "public, max-age=$maxAge")
                    .build()
            } else {
                val maxStale = 60 * 60 * 24 * 7 // tolerate 1-weeks stale
                originalResponse.newBuilder().header(
                    "Cache-Control",
                    "public, only-if-cached, max-stale=$maxStale"
                ).build()
            }

        }

        val myCache = Cache(context.cacheDir, cacheSize)

        return OkHttpClient.Builder()
            .addNetworkInterceptor(CACHE_CONTROL_INTERCEPTOR)
            .addInterceptor(getHttpLoggingInterceptor())
            .cache(myCache)
            .build()

    }

    fun hasNetworkOld(context: Context): Boolean {
        var isConnected: Boolean = false // Initial Value
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnected)
            isConnected = true
        return isConnected
    }

    fun hasNetwork(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val connection = connectivityManager.getNetworkCapabilities(network)
            return connection != null && (
                    connection.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            connection.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
        } else {
            val activeNetwork = connectivityManager.activeNetworkInfo
            if (activeNetwork != null) {
                return (activeNetwork.type == ConnectivityManager.TYPE_WIFI ||
                        activeNetwork.type == ConnectivityManager.TYPE_MOBILE)
            }
            return false
        }
    }

    private fun getHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Log.e("HttpLoggingInterceptor", message)
            }
        }).apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }
    }

}