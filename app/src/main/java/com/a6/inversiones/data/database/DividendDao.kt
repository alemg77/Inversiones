package com.a6.inversiones.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DividendDao {

    @Query("SELECT * FROM dividend")
    suspend fun getAll(): List<Dividend>

    @Query("SELECT * FROM dividend WHERE symbol=(:symbol) ")
    suspend fun getAll(symbol: String): List<Dividend>

    @Insert
    suspend fun insert(vararg dividend: Dividend)

    @Delete
    suspend fun delete(dividend: Dividend)

}