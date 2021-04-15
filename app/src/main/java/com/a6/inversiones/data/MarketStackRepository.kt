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

    suspend fun getDB(data: StockData): List<StockData>? {
        return db?.stockDao()?.get(data.symbol, data.date)
    }


    suspend fun getNewData(symbols: String): DataResult<out Any> {

        return when (val response = apiService.getEndOfDay(symbols)) {
            is DataResult.Error -> {
                response
            }
            is DataResult.Success -> {
                val data = (response.data as EndOfDayResponse).data
                var newValue = 0
                for (d in data) {

                    // Transformo responde en database
                    val stockData = StockData(d.date.substring(0, 10), d.adjClose, d.symbol)

                    // Verifica si el valor esta en la base de datos y lo agrega si no esta
                    if (getDB(stockData).isNullOrEmpty()) {
                        newValue++;
                        db?.stockDao()?.insert(stockData)
                    }

                }
                Log.d(TAG, "Agregarmos $newValue valores nuevos a $symbols")
                DataResult.Success(newValue)
            }
        }
    }

    suspend fun getNewData(symbols: String, dateFrom: String, dateTo: String): DataResult<out Any> {

        return when (val response = apiService.getEndOfDay(symbols, dateFrom, dateTo)) {
            is DataResult.Error -> {
                response
            }
            is DataResult.Success -> {
                val data = (response.data as EndOfDayResponse).data
                var newValue = 0
                for (d in data) {

                    // Transformo responde en database
                    val stockData = StockData(d.date.substring(0, 10), d.close, d.symbol)

                    // Verifica si el valor esta en la base de datos y lo agrega si no esta
                    if (getDB(stockData).isNullOrEmpty()) {
                        newValue++;
                        db?.stockDao()?.insert(stockData)
                    }

                }
                Log.d(TAG, "Agregarmos $newValue valores nuevos")
                DataResult.Success(newValue)
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