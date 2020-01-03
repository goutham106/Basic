package com.gm.basic

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET

/**
 * Author     : Gowtham
 * Email      : goutham.gm11@gmail.com
 * Github     : https://github.com/goutham106
 * Created on : 2020-01-02.
 */
interface Api {

    @GET("feed.json")
    suspend fun getFeeds(): Response<BaseData>
}