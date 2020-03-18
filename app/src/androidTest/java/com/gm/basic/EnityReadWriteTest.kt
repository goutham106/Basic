package com.gm.basic

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gm.basic.room.AppDatabase
import com.gm.basic.room.DataDAO
import com.gm.basic.room.DataEntity
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * Author     : Gowtham
 * Email      : goutham.gm11@gmail.com
 * Github     : https://github.com/goutham106
 * Created on : 18/03/20.
 */
@RunWith(AndroidJUnit4::class)
class EnityReadWriteTest {
    private lateinit var dataDAO: DataDAO
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        dataDAO = db.DataDAO()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeUserAndReadInList() {
        val dataEntity = DataEntity(
            2, 0, "title", "desc", "img_url"
            , "img_baner", "web_link", 12312.12, 232131.12, 1, 2, 3, 1, "date", null
        )
        dataDAO.insertAll(dataEntity)
        val todoItem = dataDAO.findByTitle(dataEntity.title)
        assertThat(todoItem, equalTo(dataEntity))
    }
}