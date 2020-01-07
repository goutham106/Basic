package com.gm.basic.data

import com.google.gson.annotations.SerializedName

/**
 * Author     : Gowtham
 * Email      : goutham.gm11@gmail.com
 * Github     : https://github.com/goutham106
 * Created on : 2020-01-02.
 */

data class BaseData(

    @SerializedName("valid") val valid : Boolean,
    @SerializedName("message") val message : String,
    @SerializedName("data") val data : List<Data>
)

data class Data(

    @SerializedName("id") val id : Int,
    @SerializedName("title") val title : String,
    @SerializedName("description") val description : String,
    @SerializedName("image_url_tumbnail") val image_url_tumbnail : String,
    @SerializedName("image_url_banner") val image_url_banner : String,
    @SerializedName("web_link") val web_link : String,
    @SerializedName("lat_location") val lat_location : Double,
    @SerializedName("lon_location") val lon_location : Double,
    @SerializedName("is_favourite") val is_favourite : Int,
    @SerializedName("is_share") val is_share : Int,
    @SerializedName("is_sold") val is_sold : Int,
    @SerializedName("available_count") val available_count : Int,
    @SerializedName("date") val date : String,
    @SerializedName("is_purchased") val is_purchased : String
)