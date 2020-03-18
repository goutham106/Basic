package com.gm.basic.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Author     : Gowtham
 * Email      : goutham.gm11@gmail.com
 * Github     : https://github.com/goutham106
 * Created on : 18/03/20.
 */
@Database(
    entities = [DataEntity::class],
    version = 3
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun DataDAO(): DataDAO

    companion object {
        @Volatile
        private var instance: AppDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
                context,
                AppDatabase::class.java, "data-list.db"
            )
            .fallbackToDestructiveMigration()
            .build()
    }
}