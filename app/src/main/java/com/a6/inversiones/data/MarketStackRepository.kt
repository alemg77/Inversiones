package com.a6.inversiones.data

import android.app.Application
import android.util.Log
import com.a6.inversiones.MainActivity.Companion.TAG
import com.a6.inversiones.data.database.StockData
import com.a6.inversiones.data.database.StockDataBase
import com.a6.inversiones.data.network.MarketStackRetrofitBuilder
import com.a6.inversiones.data.network.models.DataResult
import com.a6.inversiones.data.network.models.EndOfDayResponse

class MarketStackRepository(app: Application) {

    private val apiService = MarketStackRetrofitBuilder()

    private val db = StockDataBase.getInstance(app)

    suspend fun getDB(symbol: String): List<StockData>? {
        return db?.stockDao()?.getAll(symbol)
    }

    suspend fun getDB(symbols: String, day: String): List<StockData>? {
        return db?.stockDao()?.get(symbols, day)
    }

    suspend fun getData(symbols: String): DataResult<out Any> {

        return when (val response = apiService.getEndOfDay(symbols)) {
            is DataResult.Error -> {
                response
            }
            is DataResult.Success -> {
                val data = (response.data as EndOfDayResponse).data
                val allData = ArrayList<StockData>()
                for (d in data) {

                    val stockData = StockData(d.date.substring(0, 10), d.close, d.symbol)
                    allData.add(stockData)

                    var newValue = 0

                    // Verifica si el valor esta en la base de datos y lo agrega si no esta
                    val today = getDB(stockData.symbol, stockData.date)
                    if (today.isNullOrEmpty()) {
                        newValue++;
                        db?.stockDao()?.insert(stockData)
                    }
                    Log.d(TAG, "Agregarmos $newValue valores nuevos")

                }

                DataResult.Success(allData)
            }
        }
    }

    /*
    suspend fun getAllData(symbols: String): DataResult<out Any> {
        val data = getData(symbols)
        when ( )
    }

     */


    suspend fun getEndOfDay(symbols: String): DataResult<out Any> {
        return apiService.getEndOfDay(symbols)
    }

    suspend fun getEndOfDay(
        symbols: String,
        dateFrom: String,
        dateTo: String
    ): DataResult<out Any> {
        return apiService.getEndOfDay(symbols, dateFrom, dateTo)
    }


    companion object {
        const val APLE = "AAPL"
        const val MERCADO_LIBRE = "MELI"
        const val JOHNSON_AND_JOHNSON = "JNJ"
        const val COCA_COLA = "KO"
    }


}