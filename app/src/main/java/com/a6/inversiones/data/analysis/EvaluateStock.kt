package com.a6.inversiones.data.analysis

import android.util.Log
import com.a6.inversiones.MainActivity.Companion.TAG
import com.a6.inversiones.data.database.StockData
import com.a6.inversiones.data.models.Estimador

class EvaluateStock {

    fun maxValue(data: List<StockData>): Double {
        val highestPrice = data.maxByOrNull { it.value } ?: return 0.0
        return highestPrice.value
    }

    fun evaluateBuy(data: List<StockData>, oscilacion: Double): Boolean {
        val coefficient = (100 - oscilacion) / 100
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

    fun calculateCoeficiente(data: List<StockData>): Estimador {

        var bestResult: Double = 0.0
        var bestOscilation: Double = 0.0

        for (i in 0..300) {
            val oscilador = (4.0 + i / 10)
            val result = testLogic(data, oscilador)
            if (result > bestResult) {
                bestResult = result
                bestOscilation = oscilador
            }
        }

        return Estimador(
            bestOscilation,
            bestResult,
            data[0].symbol,
            100 * data[0].value / maxValue(data)
        )

    }

    fun testLogic(data: List<StockData>, oscilacion: Double): Double {

        var money = 100.0
        var valueLastBuy = 0.0
        var valueLastSell = 0.0
        var stock = 0.0

        for (i in 0..data.size - MIN_DAY_ANALISIS) {
            val k = data.size - MIN_DAY_ANALISIS - i
            val subData = data.subList(k, data.size)

            if (money > 0) {
                if (evaluateBuy(subData, oscilacion)) {
                    stock = (money * CONSTANTE_COMISION) / subData[0].value
                    money = 0.0
                    valueLastBuy = subData[0].value

                    /*
                    Log.d(TAG, "Compramos a $valueLastBuy el ${subData[0].date}")
                    val cap = stock * subData[0].value * CONSTANTE_COMISION + money

                    Log.d(
                    TAG,
                    "${subData[0].date}: money = $money , stock = $stock , capitalizacion = $cap "
                    )
                    */

                }
            } else {
                if (evaluateRetire(subData, valueLastBuy)) {
                    valueLastSell = subData[0].value
                    money = stock * valueLastSell * CONSTANTE_COMISION
                    stock = 0.0

                    /*
                    Log.d(TAG, "Vendemos a $valueLastSell el ${subData[0].date} ")
                    val cap = stock * valueLastSell * CONSTANTE_COMISION + money
                    Log.d(
                        TAG,
                        "${subData[0].date}: money = $money , stock = $stock , capitalizacion = $cap "
                    )
                     */
                }
            }
        }
        return stock * data[0].value * CONSTANTE_COMISION + money
    }

    fun testLogicVerbose(data: List<StockData>, oscilacion: Double): Double {

        var money = 100.0
        var valueLastBuy = 0.0
        var valueLastSell = 0.0
        var stock = 0.0

        Log.d(TAG, "Inicio de test en ${data[0].symbol}  con coeficiente = $oscilacion")

        for (i in 0..data.size - MIN_DAY_ANALISIS) {
            val k = data.size - MIN_DAY_ANALISIS - i
            val subData = data.subList(k, data.size)

            if (money > 0) {
                if (evaluateBuy(subData, oscilacion)) {
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
                    money = stock * valueLastSell * CONSTANTE_COMISION
                    stock = 0.0

                    Log.d(TAG, "Vendemos a $valueLastSell el ${subData[0].date} ")
                    val cap = stock * valueLastSell * CONSTANTE_COMISION + money
                    Log.d(
                        TAG,
                        "${subData[0].date}: money = $money , stock = $stock , capitalizacion = $cap "
                    )
                }
            }
        }
        val resultado = stock * data[0].value * CONSTANTE_COMISION + money

        Log.d(TAG, "Resultado final $resultado")

        return stock * data[0].value * CONSTANTE_COMISION + money
    }

    companion object {
        const val MIN_DAY_ANALISIS = 45
        const val CONSTANTE_COMISION: Double = 0.98
    }

}