package com.a6.inversiones

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a6.inversiones.MainActivity.Companion.TAG
import com.a6.inversiones.data.StockRepository
import com.a6.inversiones.data.analysis.EvaluateStock
import com.a6.inversiones.data.analysis.EvaluateStock.Companion.COEFICIENTE_NO_VENDER_SI_VOY_GANADO
import com.a6.inversiones.data.analysis.EvaluateStock.Companion.COEFIENTE_NO_COMPRAR_CUANDO_CAE
import com.a6.inversiones.data.analysis.EvaluateStock.Companion.CONSTANTE_COMISION
import com.a6.inversiones.data.models.TestResult
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import kotlin.math.pow

class EstimatorViewModel : ViewModel(), KoinComponent {

    private val marketStockRepository: StockRepository by inject()

    private var coeficientes: MutableList<TestResult> = mutableListOf()

    private val evaluate = EvaluateStock(
        80,
        CONSTANTE_COMISION,
        COEFICIENTE_NO_VENDER_SI_VOY_GANADO,
        COEFIENTE_NO_COMPRAR_CUANDO_CAE
    )

    fun evalueteCoeficiente(symbol: List<String>) {

        val buy = 0.15
        val sell = 0.15

        viewModelScope.launch {


            for (i in symbol.indices) {

                val db = marketStockRepository.getStockValue(symbol[i])

                db?.let {
                    val test = evaluate.testLogic(db, buy, sell)
                    if (test.daysInvested > 0) {
                        coeficientes.add(test)
                    }
                    //Log.d(TAG, " ${symbol[i]} rindio ${test.result} ")
                }
            }

            // Ordeno de menor a mallor
            coeficientes.sortBy { it.result }

            // Contemplo haber perdido los mejores por seguridad
            for (num in 1..(coeficientes.size / 10)) {
                Log.e(TAG, "Removed!!")
                coeficientes.removeLast()
            }

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