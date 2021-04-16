package com.a6.inversiones

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.a6.inversiones.MainActivity.Companion.TAG
import com.a6.inversiones.data.MarketStackRepository
import com.a6.inversiones.data.analysis.EvaluateStock
import com.a6.inversiones.data.analysis.EvaluateStock.Companion.COEFICIENTE_NO_VENDER_SI_VOY_GANADO
import com.a6.inversiones.data.analysis.EvaluateStock.Companion.COEFIENTE_NO_COMPRAR_CUANDO_CAE
import com.a6.inversiones.data.analysis.EvaluateStock.Companion.CONSTANTE_COMISION
import com.a6.inversiones.data.analysis.EvaluateStock.Companion.MIN_DAY_ANALISIS
import com.a6.inversiones.data.models.Estimador
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import kotlin.math.pow

class EstimatorViewModel : ViewModel(), KoinComponent {

    private val marketStockRepository: MarketStackRepository by inject()

    private var coeficientes: MutableList<Estimador> = mutableListOf()

    private val evaluate = EvaluateStock(
        MIN_DAY_ANALISIS,
        CONSTANTE_COMISION,
        COEFICIENTE_NO_VENDER_SI_VOY_GANADO,
        COEFIENTE_NO_COMPRAR_CUANDO_CAE
    )

    fun evalueteCoeficiente(symbol: List<String>) {

        val buy = 0.15
        val sell = 0.15

        viewModelScope.launch {

            var values = 0
            var accumulateResult = 0.0
            var accumumateDays = 0

            for (i in symbol.indices) {

                val db = marketStockRepository.getDB(symbol[i])

                db?.let {

                    val test = evaluate.testLogic(db, buy, sell)

                    if (test.daysInvested > 0) {
                        values++
                        accumulateResult += test.result
                        accumumateDays += test.daysInvested
                    }
                    Log.d(TAG, " ${symbol[i]} rindio ${test.result} ")
                }
            }

            val redimientoPromedio = accumulateResult / values

            val averageDays = accumumateDays / values

            Log.d(TAG, "Resultado rindio $redimientoPromedio y trabajo $averageDays ")

            val aux3 = (redimientoPromedio / 100.0).pow(260.0 / averageDays)

            Log.e(TAG, " $aux3")

            Log.d(TAG, "Fin de los calculos coeficientes")

            for (i in symbol.indices) {
                val db = marketStockRepository.getDB(symbol[i])
                db?.let {
                    val evaluateBuy = evaluate.evaluateBuy(db, buy)
                    if (evaluateBuy > 0) {
                        Log.d(TAG, "Compra: ${symbol[i]}  $evaluateBuy")
                    }
                }
            }

            Log.d(TAG, "Fin de los calculos")
        }
    }

}