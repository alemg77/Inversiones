package com.a6.inversiones.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [StockValue::class, Dividend::class], version = 2)

abstract class StockDataBase() : RoomDatabase() {

    abstract fun stockDao(): StockValueDao

    abstract fun dividendDao(): DividendDao

    companion object {
        private const val DATABASE_NAME = "ensayo"

        @Volatile
        private var INSTANCE: StockDataBase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE dividend (id INTEGER PRIMARY KEY NOT NULL, symbol TEXT NOT NULL, value REAL NOT NULL, date TEXT NOT NULL )")
                database.execSQL("ALTER TABLE stockData RENAME TO stockValue")
            }
        }

        fun getInstance(context: Context): StockDataBase? {
            INSTANCE ?: synchronized(this) {
                INSTANCE = Room
                    .databaseBuilder(
                        context.applicationContext,
                        StockDataBase::class.java,
                        DATABASE_NAME
                    )
                    .addMigrations(MIGRATION_1_2)
                    .build()
            }
            return INSTANCE
        }
    }


}