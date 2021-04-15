package com.a6.inversiones

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a6.inversiones.data.MarketStackRepository
import com.a6.inversiones.data.SharedPreferencesManager
import com.a6.inversiones.data.SharedPreferencesManager.Companion.SYMBOLS
import com.a6.inversiones.data.analysis.EvaluateStock
import com.a6.inversiones.data.database.StockData
import com.a6.inversiones.data.models.Estimador
import com.a6.inversiones.data.network.models.DataResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class MarketStockViewModel : ViewModel(), KoinComponent {

    private val marketStockRepository: MarketStackRepository by inject()

    private val sharedPreferencesManager: SharedPreferencesManager by inject()

    private var coeficientes: MutableList<Estimador> = mutableListOf()

    fun getAllData() {
        viewModelScope.launch(Dispatchers.IO) {
            for (i in SYMBOLS.indices) {

                val db = marketStockRepository.getDB(SYMBOLS[i])
                if (db.isNullOrEmpty()) {
                    getNewData(SYMBOLS[i])
                } else {
                    Log.d(TAG, "Ya tenemos info de ${SYMBOLS[i]}")
                }
            }
        }
    }

    fun buscarMinimos() {
        viewModelScope.launch {
            for (i in SYMBOLS.indices) {
                val db: List<StockData> = marketStockRepository.getDB(SYMBOLS[i])!!
                val evaluateStock = EvaluateStock()
                val maxValue = evaluateStock.maxValue(db)
                val actual = 100 * db[0].value / maxValue
                Log.d(TAG, " ${SYMBOLS[i]} esta al $actual%")
            }
        }
    }

    fun calculateAllCoeficiente() {
        viewModelScope.launch {
            for (i in SYMBOLS.indices) {

                val db: List<StockData> = marketStockRepository.getDB(SYMBOLS[i])!!
                val evaluateStock = EvaluateStock()
                val cof = evaluateStock.calculateCoeficiente(db)

                if ((cof.resultado > RESULTADO_MINIMO)
                    && (cof.oscilador > OSCILADOR_MINIMO)
                    && (cof.valuex100 < VALOR_100_MAXIMO)
                ) {
                    coeficientes.add(cof)
                }

                Log.d(
                    TAG,
                    "Resultado final: ${cof.symbol} rindio ${cof.resultado} con ${cof.oscilador}"
                )
            }

            coeficientes.sortBy { it.valuex100 }

            Log.d(TAG, "Fin de los calculos")
        }
    }

    fun calculateCoeficiente(symbol: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val db: List<StockData> = marketStockRepository.getDB(symbol)!!
            val evaluateStock = EvaluateStock()
            val cof = evaluateStock.calculateCoeficiente(db)
            coeficientes.add(cof)
            sharedPreferencesManager.saveOscilacion(symbol, cof.oscilador)
            Log.d(
                TAG,
                "Resultado final: $symbol rindio ${cof.resultado} con ${cof.oscilador}"
            )
        }
    }

    fun testCoeficiente(symbol: String, cof: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            val db: List<StockData> = marketStockRepository.getDB(symbol)!!
            val evaluateStock = EvaluateStock()
            evaluateStock.testLogicVerbose(db, cof)
        }
    }

    fun getNewData(symbol: String) {
        viewModelScope.launch(Dispatchers.IO) {

            when (marketStockRepository.getNewData(symbol)) {
                is DataResult.Success -> {

                    /*
                    val db = marketStockRepository.getDB(symbol)
                    db?.let {
                        val evaluator = EvaluateStock()
                        evaluator.testLogic(db, 10.0)
                    }
                     */

                }
                else -> {
                    Log.e(TAG, " No se pudo obtener los datos de la API de: $symbol")
                }
            }

        }
    }

    companion object {
        const val TAG = "TAGG VIEW MODEL"
        const val RESULTADO_MINIMO = 106.0
        const val OSCILADOR_MINIMO = 7.0
        const val VALOR_100_MAXIMO = 90
    }

}


