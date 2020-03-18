package com.gm.basic.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Author     : Gowtham
 * Email      : goutham.gm11@gmail.com
 * Github     : https://github.com/goutham106
 * Created on : 18/03/20.
 */
@Entity(tableName = "data_items")
data class DataEntity(
    @PrimaryKey(autoGenerate = true)
    var count: Int,
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "image_url_tumbnail") val image_url_tumbnail: String,
    @ColumnInfo(name = "image_url_banner") val image_url_banner: String,
    @ColumnInfo(name = "web_link") val web_link: String,
    @ColumnInfo(name = "lat_location") val lat_location: Double,
    @ColumnInfo(name = "lon_location") val lon_location: Double,
    @ColumnInfo(name = "is_favourite") val is_favourite: Int,
    @ColumnInfo(name = "is_share") val is_share: Int,
    @ColumnInfo(name = "is_sold") val is_sold: Int,
    @ColumnInfo(name = "available_count") val available_count: Int,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "is_purchased") val is_purchased: String?
)