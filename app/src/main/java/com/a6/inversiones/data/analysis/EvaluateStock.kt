package com.a6.inversiones.data.analysis

import android.util.Log
import com.a6.inversiones.MainActivity.Companion.TAG
import com.a6.inversiones.data.database.StockData

class EvaluateStock {

    fun maxValue(data: List<StockData>): Double {
        val highestPrice = data.maxByOrNull { it.value } ?: return 0.0
        return highestPrice.value
    }

    fun evaluateBuy(data: List<StockData>): Boolean {
        val coefficient = (100 - CAIDA_COMPRA_PORCIENTO) / 100
        val test = maxValue(data) * coefficient
        return data[0].value < test
    }

    fun evaluateRetire(data: List<StockData>, lastBuyValue: Double): Boolean {
        val actualValue = data[0].value
        val retireValue = lastBuyValue * 1.12
        val maxValue = maxValue(data)

        if (actualValue < maxValue) {
            return false
        }

        if (actualValue > retireValue) {
            return true
        }

        return false
    }

    fun testLogic(data: List<StockData>) {

        var money = 100.0
        var valueLastBuy = 0.0
        var valueLastSell = 0.0
        var stock = 0.0

        for (i in 0..data.size - MIN_DAY_ANALISIS) {
            val k = data.size - MIN_DAY_ANALISIS - i
            val subData = data.subList(k, data.size)

            if (money > 0) {
                if (evaluateBuy(subData)) {
                    stock = (money * CONSTANTE_COMISION) / subData[0].value
                    money = 0.0
                    valueLastBuy = subData[0].value

                    Log.d(TAG, "Compramos a $valueLastBuy el ${subData[0].date}")
                    val cap = stock * subData[0].value * CONSTANTE_COMISION + money
                    Log.d(
                        TAG,
                        "${subData[0].date}: money = $money , stock = $stock , capitalizacion = $cap "
                    )

                }
            } else {
                if (evaluateRetire(subData, valueLastBuy)) {
                    valueLastSell = subData[0].value
                    Log.d(TAG, "Vendemos a $valueLastSell el ${subData[0].date} ")
                    money = stock * valueLastSell * CONSTANTE_COMISION
                    stock = 0.0
                    val cap = stock * valueLastSell * CONSTANTE_COMISION + money
                    Log.d(
                        TAG,
                        "${subData[0].date}: money = $money , stock = $stock , capitalizacion = $cap "
                    )
                }
            }

        }

        val cap = stock * data[0].value * CONSTANTE_COMISION + money
        Log.d(TAG, " ******** Resultado Final: $cap  *********************")

    }

    companion object {
        const val MIN_DAY_ANALISIS = 30
        const val CAIDA_COMPRA_PORCIENTO: Double = 16.0
        const val CONSTANTE_COMISION: Double = 0.98
    }

}