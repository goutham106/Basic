package com.gm.basic.data

import android.content.Context
import com.gm.basic.room.DataEntity
import com.gm.basic.utils.Utils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Author     : Gowtham
 * Email      : goutham.gm11@gmail.com
 * Github     : https://github.com/goutham106
 * Created on : 2020-01-02.
 */
class Repository {

    val moview: ArrayList<Data> = ArrayList()
    val list: MutableList<DataEntity> = mutableListOf()

    suspend fun getFeed(context: Context): List<DataEntity> {
        val db = Utils.getDBService(context)
        list.clear()
        if (Utils.hasNetwork(context)) {
            val response = Utils.getApiService(context).getFeeds()

            if (response.isSuccessful) {
                val listData = response.body()?.data
                listData?.let { list.addAll(it) }

                if (listData != null) {
                    for (data in listData) {
                        GlobalScope.launch {
                            db.DataDAO().insertAll(data)
                        }
                    }
                }

            }
        } else {
            GlobalScope.launch {
                list.addAll(db.DataDAO().getAll())
                //Log.e("DB", list.toString())
            }
        }
        Thread.sleep(1000)
        return list
    }

}