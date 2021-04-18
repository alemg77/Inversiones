package com.a6.inversiones.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface StockValueDao {

    @Query("SELECT * FROM StockValue")
    suspend fun getAll(): List<StockValue>

    @Query("SELECT * FROM stockValue WHERE symbol=(:symbol) ")
    suspend fun getAll(symbol: String): List<StockValue>

    @Query("SELECT * FROM stockValue WHERE symbol=(:symbol) AND date=(:day) ")
    suspend fun get(symbol: String, day: String): List<StockValue>

    @Insert
    suspend fun insert(vararg stockValue: StockValue)

    @Delete
    suspend fun delete(stockValue: StockValue)

}