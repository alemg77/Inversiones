package com.a6.inversiones

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a6.inversiones.data.StockRepository
import com.a6.inversiones.data.analysis.EvaluateStock
import com.a6.inversiones.data.models.AnalisisResult
import com.a6.inversiones.data.models.AnalisisStockValue
import com.a6.inversiones.data.models.TestResult
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import kotlin.math.pow

class EstimatorViewModel : ViewModel(), KoinComponent {

    private val marketStockRepository: StockRepository by inject()


    private val evaluate = EvaluateStock()


    private val _analisis = MutableLiveData<AnalisisResult>()
    val analisis: LiveData<AnalisisResult> = _analisis


    private val _resultado = MutableLiveData<Double>()
    val resultado: LiveData<Double> = _resultado

    private fun loadUsers() {
        // Do an asynchronous operation to fetch users.
    }

    private suspend fun extraRiskBuy(symbol: String): Double {
        val dividend = marketStockRepository.getDividend(symbol)
        return if (dividend == 0.0) {
            0.03
        } else if (dividend < 0.50) {
            0.025
        } else if (dividend < 1.00) {
            0.02
        } else if (dividend < 2.00) {
            0.01
        } else {
            0.00
        }
    }

    fun evaluateBuy(symbol: List<String>, buy: Double) {
        viewModelScope.launch {
            for (i in symbol.indices) {
                val data = marketStockRepository.getStockValue(symbol[i])
                val dividend = marketStockRepository.getDividend(symbol[i])
                if (!data.isNullOrEmpty()) {
                    val evaluation = evaluate.evaluateBuy(data, buy + extraRiskBuy(symbol[i]))
                    if (evaluation > 1) {
                        _analisis.postValue(AnalisisResult(symbol[i], evaluation, dividend))
                    }
                }
            }
        }
    }

    fun evaluarCoeficiente(symbol: List<String>) {
        viewModelScope.launch {

            var bestresult = 0.0
            for (i in 0..40) {
                var buy = 0.10 + i.toDouble() / 200
                val result = evaluarCoeficiente(symbol, buy, buy)
                if (result > bestresult) {
                    bestresult = result
                    Log.e(TAG, "Hasta ahora el mejor resultado es $bestresult con $buy")
                }
            }

        }
    }

    fun buscarCoeficiente(data: MutableList<AnalisisStockValue>): Double {

        var bestCoeficiente = 0.10
        var bestResult = 0.0
        for (i in 10..40) {
            val coef = i.toDouble() / 100
            val result = evaluate.testLogic(data, coef, coef)
            if (result.result > bestResult) {
                bestResult = result.result
                bestCoeficiente = coef
                Log.d(TAG, "$coef  rindio $result, invirtio ${result.timesInverted} veces")
            }
        }
        return bestCoeficiente
    }


    fun evaluarCoeficiente(
        data: MutableList<AnalisisStockValue>,
        buy: Double,
        sell: Double
    ): Double {

        if (data.isNullOrEmpty()) {
            Log.e(TAG, "No hay datos")
            return 0.0
        }

        val testLogic = evaluate.testLogic(data, buy, sell)

        return testLogic.rendimientoAnual()
    }

    suspend fun evaluarCoeficiente(symbol: List<String>, buy: Double, sell: Double): Double {

        val coeficientes: MutableList<TestResult> = mutableListOf()

        for (i in symbol.indices) {

            val data = marketStockRepository.getStockValue(symbol[i])

            if (data.isNullOrEmpty()) {
                Log.e(TAG, " No se encontraron dados de ${symbol[i]}")
                TODO()
            } else {
                val test = evaluate.testLogic(data, buy + extraRiskBuy(symbol[i]), sell)
                if (test.daysInvested > 0) {
                    coeficientes.add(test)
                    // Log.d(TAG, " ${symbol[i]} rindio ${test.result} ")
                }
            }
        }

        // Menos de 5 inversiones es peligroso, no hay que hacer nada
        if (coeficientes.size < 5) {
            return 0.0
        }

        /*
        // Ordeno de menor a mallor
        coeficientes.sortBy { it.result }

        // Contemplo haber perdido los mejores por seguridad
        for (num in 1..(coeficientes.size / 10)) {
            //Log.e(TAG, "Removed!!")
            coeficientes.removeLast()
        }
        Log.d(TAG, "Los peores fueron:")
        for (i in 0..4) {
            Log.d(TAG, "${coeficientes[i].symbol}  rindio ${coeficientes[i].result} ")
        }
         */

        var accumulateResult = 0.0
        var accumulateDays = 0

        coeficientes.forEach {
            accumulateResult += it.result
            accumulateDays += it.daysInvested
        }

        val redimientoPromedio = accumulateResult / coeficientes.size

        val averageDays = accumulateDays.toDouble() / coeficientes.size

        Log.d(
            TAG,
            "Inverti en ${coeficientes.size} empresas, rindio $redimientoPromedio y trabajo $averageDays "
        )

        val size = coeficientes.size.toDouble()

        //return redimientoPromedio
        return ((redimientoPromedio / 100.0).pow(200.0 / averageDays)) * (coeficientes.size) * size.pow(
            0.4
        )
    }

    companion object {
        const val TAG = "TAGGG_RESULTADO"
    }

}

/*
            for (i in symbol.indices) {
                val db = marketStockRepository.getDB(symbol[i])
                db?.let {
                    val evaluateBuy = evaluate.evaluateBuy(db, buy)
                    if (evaluateBuy > 0) {
                        Log.d(TAG, "Compra: ${symbol[i]}  $evaluateBuy")
                    }
                }
            }
 */