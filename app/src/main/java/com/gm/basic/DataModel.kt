package com.gm.basic

import com.google.gson.annotations.SerializedName

/**
 * Author     : Gowtham
 * Email      : goutham.gm11@gmail.com
 * Github     : https://github.com/goutham106
 * Created on : 2020-01-02.
 */

data class BaseData(

    @SerializedName("status") val status: String,
    @SerializedName("error") val error: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<Data>
)

data class Data(

    @SerializedName("title") val title: String,
    @SerializedName("pubDate") val pubDate: String,
    @SerializedName("link") val link: String,
    @SerializedName("author") val author: String,
    @SerializedName("thumbnail") val thumbnail: String,
    @SerializedName("description") val description: String
)