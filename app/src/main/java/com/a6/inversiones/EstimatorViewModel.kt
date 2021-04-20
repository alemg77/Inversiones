package com.a6.inversiones

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a6.inversiones.MainActivity.Companion.TAG
import com.a6.inversiones.data.StockRepository
import com.a6.inversiones.data.analysis.EvaluateStock
import com.a6.inversiones.data.models.TestResult
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import kotlin.math.pow

class EstimatorViewModel : ViewModel(), KoinComponent {

    private val marketStockRepository: StockRepository by inject()

    private var coeficientes: MutableList<TestResult> = mutableListOf()

    private val evaluate = EvaluateStock()

    private suspend fun extraRiskBuy(symbol: String): Double {
        val dividend = marketStockRepository.getDividend(symbol)
        return if (dividend == 0.0) {
            0.02
        } else if (dividend < 0.05) {
            0.01
        } else {
            0.00
        }
    }


    fun evaluateBuy(symbol: List<String>, buy: Double) {
        viewModelScope.launch {
            for (i in symbol.indices) {
                val db = marketStockRepository.getStockValue(symbol[i])
                if (!db.isNullOrEmpty()) {
                    val evaluation = evaluate.evaluateBuy(db, buy + extraRiskBuy(symbol[i]))
                    if (evaluation > 0) {
                        Log.d(TAG, "Comprar ${symbol[i]} , confianza: $evaluation")
                    }
                }
            }
        }
    }

    fun evalueteCoeficiente(symbol: List<String>) {

        var buy = 0.15
        val sell = 0.15

        viewModelScope.launch {


            for (i in symbol.indices) {

                val db = marketStockRepository.getStockValue(symbol[i])

                val dividend = marketStockRepository.getDividend(symbol[i])

                db?.let {
                    val test = evaluate.testLogic(db, buy + extraRiskBuy(symbol[i]), sell)
                    if (test.daysInvested > 0) {
                        coeficientes.add(test)
                        Log.d(TAG, " ${symbol[i]} rindio ${test.result} ")
                    }
                }
            }

            // Ordeno de menor a mallor
            coeficientes.sortBy { it.result }

            // Contemplo haber perdido los mejores por seguridad
            /*
            for (num in 1..(coeficientes.size / 10)) {
                Log.e(TAG, "Removed!!")
                coeficientes.removeLast()
            }
             */

            Log.d(TAG, "Los peores fueron:")
            for (i in 0..4) {
                Log.d(TAG, "${coeficientes[i].symbol}  rindio ${coeficientes[i].result} ")
            }

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

            val aux3 = (redimientoPromedio / 100.0).pow(260.0 / averageDays)

            Log.e(TAG, " $aux3")

            Log.d(TAG, "Fin de los calculos coeficientes")

            Log.d(TAG, "Fin de los calculos")
            Log.d(TAG, "Fin de los calculos")

        }
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