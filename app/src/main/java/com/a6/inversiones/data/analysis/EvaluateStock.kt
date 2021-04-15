package com.a6.inversiones.data.analysis

import com.a6.inversiones.data.database.StockData
import com.a6.inversiones.data.models.TestResult

class EvaluateStock {

    fun maxValue(data: List<StockData>): Double {
        val highestPrice = data.maxByOrNull { it.value } ?: return 0.0
        return highestPrice.value
    }

    fun evaluateBuy(data: List<StockData>, buy: Double): Boolean {
        val test = maxValue(data) * (1 - buy)

        if ((data[0].value * COEFIENTE_NO_COMPRAR_CUANDO_CAE) < data[1].value) {
            return false
        }

        return data[0].value < test
    }

    private fun evaluateRetire(data: List<StockData>, lastBuyValue: Double, sell: Double): Boolean {
        val actualValue = data[0].value


        // Si vengo subiendo, no vendo
        if (data[0].value > (data[1].value * COEFICIENTE_NO_VENDER_SI_VOY_GANADO)) {
            return false
        }


        val retireValue = lastBuyValue * (1 + sell)
        return actualValue > retireValue
    }

    /*
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

     */

    fun testLogic(data: List<StockData>, buy: Double, sell: Double): TestResult {

        var money = 100.0
        var valueLastBuy = 0.0
        var valueLastSell = 0.0
        var stock = 0.0
        var daysInverted = 0

        for (i in 0..data.size - MIN_DAY_ANALISIS) {

            val k = data.size - MIN_DAY_ANALISIS - i
            val subData = data.subList(k, data.size)

            if (subData[subData.size - 1].value < subData[subData.size - MIN_DAY_ANALISIS].value) {
                if (money > 0) {
                    if (evaluateBuy(subData, buy)) {
                        stock = (money * CONSTANTE_COMISION) / subData[0].value
                        money = 0.0
                        valueLastBuy = subData[0].value
                    }
                } else {
                    if (evaluateRetire(subData, valueLastBuy, sell)) {
                        valueLastSell = subData[0].value
                        money = stock * valueLastSell * CONSTANTE_COMISION
                        stock = 0.0
                    }
                }
            }


            if (money == 0.0) {
                daysInverted++
            }
        }

        return TestResult(daysInverted, stock * data[0].value * CONSTANTE_COMISION + money)

    }

    /*
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

     */

    companion object {
        const val MIN_DAY_ANALISIS = 80
        const val CONSTANTE_COMISION: Double = 0.98 // 1,5% de comision y 0,5% de spreed
        const val COEFICIENTE_NO_VENDER_SI_VOY_GANADO = 1.01
        const val COEFIENTE_NO_COMPRAR_CUANDO_CAE = 1.25
    }

}