package com.a6.inversiones.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [StockData::class], version = 1)

abstract class StockDataBase() : RoomDatabase() {

    abstract fun stockDao(): StockDao


    companion object {
        private const val DATABASE_NAME = "ensayo"

        @Volatile
        private var INSTANCE: StockDataBase? = null

        fun getInstance(context: Context): StockDataBase? {
            INSTANCE ?: synchronized(this) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    StockDataBase::class.java,
                    DATABASE_NAME
                ).build()
            }
            return INSTANCE
        }
    }
}