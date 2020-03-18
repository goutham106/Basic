package com.gm.basic.room

import androidx.room.*

/**
 * Author     : Gowtham
 * Email      : goutham.gm11@gmail.com
 * Github     : https://github.com/goutham106
 * Created on : 18/03/20.
 */
@Dao
interface DataDAO {
    @Query("SELECT * FROM data_items")
    fun getAll(): List<DataEntity>

    @Query("SELECT * FROM data_items WHERE title LIKE :title")
    fun findByTitle(title: String): DataEntity

    @Insert
    fun insertAll(vararg data: DataEntity)

    @Delete
    fun delete(data: DataEntity)

    @Update
    fun updateTodo(vararg datas: DataEntity)

    @Query("DELETE FROM data_items")
    fun deleteAll()
}