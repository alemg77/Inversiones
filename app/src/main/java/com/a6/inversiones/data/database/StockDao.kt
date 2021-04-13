package com.a6.inversiones.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface StockDao {

    @Query("SELECT * FROM stockData")
    suspend fun getAll(): List<StockData>

    @Query("SELECT * FROM stockData WHERE symbol=(:symbol) ")
    suspend fun getAll(symbol: String): List<StockData>


    @Insert
    suspend fun insert(vararg medicions: StockData)


    @Delete
    suspend fun delete(medicion: StockData)

}