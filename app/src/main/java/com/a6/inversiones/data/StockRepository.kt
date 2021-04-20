package com.a6.inversiones.data

import android.app.Application
import android.util.Log
import com.a6.inversiones.MainActivity.Companion.TAG
import com.a6.inversiones.data.database.Dividend
import com.a6.inversiones.data.database.StockDataBase
import com.a6.inversiones.data.database.StockValue
import com.a6.inversiones.data.network.MarketStackRetrofitBuilder
import com.a6.inversiones.data.network.models.DataResult
import com.a6.inversiones.data.network.models.EndOfDayResponse
import com.a6.inversiones.data.network.models.YahooFinance
import java.time.LocalDateTime

class StockRepository(app: Application) {

    private val apiMarketStack = MarketStackRetrofitBuilder()

    private val apiYahoo = YahooFinance()

    private val db = StockDataBase.getInstance(app)

    suspend fun getStockValue(symbol: String): List<StockValue>? {
        return db?.stockValueDao()?.getAll(symbol)
    }

    suspend fun getStockValue(data: StockValue): List<StockValue>? {
        return db?.stockValueDao()?.get(data.symbol, data.date)
    }

    suspend fun getDividend(symbol: String): Double {
        val dividens = db?.dividendDao()?.get(symbol)
        return if (dividens.isNullOrEmpty()) {
            0.0
        } else {
            dividens[0].value
        }
    }

    suspend fun getAllDivident(): DataResult<out Any> {
        val dividens = db?.dividendDao()?.getAll()
        return if (dividens.isNullOrEmpty()) {
            DataResult.Error("No data")
        } else {
            DataResult.Success(dividens)
        }
    }

    suspend fun getDividendToday(symbol: String): DataResult<out Any> {
        return when (val response = apiYahoo.getGeneric(symbol)) {
            "null" -> {
                Log.e(TAG, "$symbol No se encuentran dividendos ")
                DataResult.Error("null")
            }
            null -> {
                Log.e(TAG, "$symbol No se encuentran dividendos ")
                DataResult.Error("null")
            }
            else -> {
                val index = response.indexOf("DIVIDEND_AND_YIELD-value", 0, false)
                val substring = response.substring(index + 45, index + 49)
                if (substring == "N/A ") {
                    Log.e(TAG, "$symbol No tiene dividendos")
                    DataResult.Error("No tiene")
                } else {
                    val localDate = LocalDateTime.now().toLocalDate()
                    val dividend = Dividend(symbol, substring.toDouble(), localDate.toString())
                    db?.dividendDao()?.insert(dividend)
                    DataResult.Success(dividend)
                }
            }
        }
    }

    suspend fun getNewStockValue(symbols: String): DataResult<out Any> {
        return when (val response = apiMarketStack.getEndOfDay(symbols)) {
            is DataResult.Error -> {
                response
            }
            is DataResult.Success -> {
                val data = (response.data as EndOfDayResponse).data
                var newValue = 0
                for (d in data) {

                    // Transformo responde en database
                    val stockData = StockValue(d.date.substring(0, 10), d.adjClose, d.symbol)

                    // Verifica si el valor esta en la base de datos y lo agrega si no esta
                    if (getStockValue(stockData).isNullOrEmpty()) {
                        newValue++;
                        db?.stockValueDao()?.insert(stockData)
                    }

                }
                Log.d(TAG, "Agregarmos $newValue valores nuevos a $symbols")
                DataResult.Success(newValue)
            }
        }
    }

    private suspend fun getNewStockValue(symbols: List<String>): DataResult<out Any> {
        var string: String = symbols[0]
        for (i in 1 until symbols.size) {
            string += ","
            string += symbols[i]
        }
        return getNewStockValue(string)
    }

    suspend fun getNewStockValue(symbols: ArrayList<String>, num: Int) {
        var i = 0
        while (i < symbols.size) {
            if (i + num > symbols.size) {
                getNewStockValue(symbols.subList(i, symbols.size))
            } else {
                getNewStockValue(symbols.subList(i, i + num))
            }
            i += 4
        }
        Log.d(TAG, "Fin de la busqueda de datos nuevos!!!")
    }


    suspend fun getNewStockValue(
        symbols: String,
        dateFrom: String,
        dateTo: String
    ): DataResult<out Any> {

        return when (val response = apiMarketStack.getEndOfDay(symbols, dateFrom, dateTo)) {
            is DataResult.Error -> {
                response
            }
            is DataResult.Success -> {
                val data = (response.data as EndOfDayResponse).data
                var newValue = 0
                for (d in data) {

                    // Transformo responde en database
                    val stockData = StockValue(d.date.substring(0, 10), d.close, d.symbol)

                    // Verifica si el valor esta en la base de datos y lo agrega si no esta
                    if (getStockValue(stockData).isNullOrEmpty()) {
                        newValue++;
                        db?.stockValueDao()?.insert(stockData)
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
        return apiMarketStack.getEndOfDay(symbols)
    }

    suspend fun getEndOfDay(
        symbols: String,
        dateFrom: String,
        dateTo: String
    ): DataResult<out Any> {
        return apiMarketStack.getEndOfDay(symbols, dateFrom, dateTo)
    }


    companion object {
        const val APLE = "AAPL"
        const val MERCADO_LIBRE = "MELI"
        const val JOHNSON_AND_JOHNSON = "JNJ"
        const val COCA_COLA = "KO"
    }


}