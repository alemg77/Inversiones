package com.a6.inversiones.data.analysis

import com.a6.inversiones.data.database.StockValue
import com.a6.inversiones.data.models.TestResult

class EvaluateStock(
    val diasMinimosAnalisis: Int,
    val comisionCompra: Double,
    val coeficienteVenderSiVoyGanando: Double,
    val coeficienteNoComprarCuandoCae: Double,
) {

    fun maxValue(data: List<StockValue>): Double {
        val highestPrice = data.maxByOrNull { it.value } ?: return 0.0
        return highestPrice.value
    }

    fun evaluateBuy(data: List<StockValue>, buy: Double): Double {
        val test = maxValue(data) * (1 - buy)

        // 4 dias callendo, no comprar
        if ((data[0].value < data[1].value)
            && (data[1].value < data[2].value)
            && (data[2].value < data[3].value)
            && (data[3].value < data[4].value)
        ) {
            return 0.0
        }

        /*
        if ((data[0].value * COEFIENTE_NO_COMPRAR_CUANDO_CAE) < data[1].value) {
            return 0.0
        }

         */


        return if (data[0].value > test) {
            0.0
        } else {
            test / data[0].value
        }

    }

    private fun evaluateRetire(
        data: List<StockValue>,
        lastBuyValue: Double,
        sell: Double
    ): Boolean {

        val actualValue = data[0].value

        // Si vengo subiendo, no vendo
        if (data[0].value > (data[1].value * COEFICIENTE_NO_VENDER_SI_VOY_GANADO)) {
            return false
        }

        val retireValue = lastBuyValue * (1 + sell)

        return actualValue > retireValue

    }

    fun testLogic(data: List<StockValue>, buy: Double, sell: Double): TestResult {

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
                    // No quiero analizar el coeficiente si no tengo datos suficientes para analizar despues que paso
                    if (data.size < subData.size + 30) {
                        if (evaluateBuy(subData, buy) > 0) {
                            stock = (money * CONSTANTE_COMISION) / subData[0].value
                            money = 0.0
                            valueLastBuy = subData[0].value
                        }
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

        val rendimiento = stock * data[0].value * CONSTANTE_COMISION + money

        return TestResult(daysInverted, rendimiento, data[0].symbol)

    }

    companion object {
        const val MIN_DAY_ANALISIS = 80
        const val CONSTANTE_COMISION: Double = 0.98 // 1,5% de comision y 0,5% de spreed
        const val COEFICIENTE_NO_VENDER_SI_VOY_GANADO = 1.009
        const val COEFIENTE_NO_COMPRAR_CUANDO_CAE = 1.10
    }

}