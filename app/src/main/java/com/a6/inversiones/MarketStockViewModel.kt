package com.a6.inversiones

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a6.inversiones.data.SharedPreferencesManager
import com.a6.inversiones.data.StockRepository
import com.a6.inversiones.data.analysis.EvaluateStock
import com.a6.inversiones.data.database.StockValue
import com.a6.inversiones.data.models.Estimador
import com.a6.inversiones.data.network.models.DataResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class MarketStockViewModel : ViewModel(), KoinComponent {

    private val stockRepository: StockRepository by inject()

    private val sharedPreferencesManager: SharedPreferencesManager by inject()

    private var coeficientes: MutableList<Estimador> = mutableListOf()


    /*
        coeficientes.sortBy { it.valuex100 }
     */

    private val evaluate = EvaluateStock(
        EvaluateStock.MIN_DAY_ANALISIS,
        EvaluateStock.CONSTANTE_COMISION,
        EvaluateStock.COEFICIENTE_NO_VENDER_SI_VOY_GANADO,
        EvaluateStock.COEFIENTE_NO_COMPRAR_CUANDO_CAE
    )


    fun test(symbol: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            var dividendos = mutableListOf<String>()
            for (i in symbol.indices) {

                when (val result = stockRepository.getDividend(symbol[i])) {
                    is DataResult.Success -> {
                        val s = result.data as String
                        dividendos.add(s)
                        Log.d(TAG, "  ${symbol[i]} tiene $s")
                    }
                    else -> {

                    }
                }
            }
            Log.d(TAG, dividendos.toString())
        }
    }


    fun getAllData(symbol: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            for (i in symbol.indices) {
                val db = stockRepository.getDB(symbol[i])
                if (db.isNullOrEmpty()) {
                    getNewData(symbol[i])
                } else {
                    Log.d(TAG, "Ya tenemos info de ${symbol[i]}")
                }
                delay(250)
            }
        }
    }

    fun buscarMinimos(symbol: List<String>) {
        viewModelScope.launch {
            for (i in symbol.indices) {
                val db: List<StockValue> = stockRepository.getDB(symbol[i])!!
                val maxValue = evaluate.maxValue(db)
                val actual = 100 * db[0].value / maxValue
                Log.d(TAG, " ${symbol[i]} esta al $actual%")
            }
        }
    }

    fun getNewData(symbol: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (stockRepository.getNewData(symbol)) {
                is DataResult.Success -> {

                }
                else -> {
                    Log.e(TAG, " No se pudo obtener los datos de la API de: $symbol")
                }
            }
        }
    }

    companion object {
        const val TAG = "TAGG VIEW MODEL"
    }

}


