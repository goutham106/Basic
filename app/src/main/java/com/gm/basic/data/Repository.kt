package com.gm.basic.data

import android.content.Context
import com.gm.basic.utils.Utils

/**
 * Author     : Gowtham
 * Email      : goutham.gm11@gmail.com
 * Github     : https://github.com/goutham106
 * Created on : 2020-01-02.
 */
class Repository {

    val moview: ArrayList<Data> = ArrayList()
    val list: MutableList<Data> = mutableListOf()

    suspend fun getFeed(context: Context): List<Data> {

        val response = Utils.getApiService(context).getFeeds()

        if (response.isSuccessful) {
            val listData = response.body()?.data
            listData?.let { list.addAll(it) }
        }

        return list
    }

}