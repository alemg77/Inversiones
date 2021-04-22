package com.a6.inversiones.data.analysis

import android.util.Log
import com.a6.inversiones.MainActivity.Companion.TAG
import com.a6.inversiones.data.database.StockValue
import com.a6.inversiones.data.models.TestResult

class EvaluateStock() {

    fun maxValue(data: List<StockValue>): Double {
        val highestPrice = data.maxByOrNull { it.value } ?: return 0.0
        return highestPrice.value
    }

    fun evaluateBuy(data: List<StockValue>, buy: Double): Double {
        val test = maxValue(data) * (1 - buy)

        var maxDrop = 0.0
        for (i in 0 until data.size - 2) {
            if (data[i].value < data[i + 2].value) {
                val drop = data[i + 2].value / data[i].value
                if (drop > maxDrop) {
                    maxDrop = drop
                }
            }
        }

        Log.d(TAG, "${data[0].symbol} tuvo un drop de $maxDrop")

        // Si se desploma un dia, no comprar
        if ((data[0].value * 1.07) < data[1].value) {
            return 0.0
        }

        // Si se desploma en dos dias, no comprar
        if ((data[0].value * 1.08) < data[2].value) {
            return 0.0
        }

        // Si se desploma en tres dias, no comprar
        if ((data[0].value * 1.09) < data[3].value) {
            return 0.0
        }

        // Muchos dias callendo, no comprar
        if ((data[0].value < data[1].value)
            && (data[1].value < data[2].value)
            && (data[2].value < data[3].value)
            && (data[3].value < data[4].value)
            && (data[4].value < data[5].value)
        ) {
            return 0.0
        }

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
        if (data[0].value > (data[1].value * 1.015)) {
            return false
        }

        // Si vengo subiendo, no vendo
        if (data[0].value > (data[2].value * 1.045)) {
            return false
        }

        // Si vengo subiendo, no vendo
        if (data[0].value > (data[3].value * 1.065)) {
            return false
        }

        // Si vengo subiendo varios dias seguidos, no vendo
        if ((data[0].value > (data[1].value))
            && ((data[1].value > (data[2].value)))
            && ((data[2].value > (data[3].value)))
            && ((data[3].value > (data[4].value)))
            && ((data[4].value > (data[5].value)))
        ) {
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

        for (i in 0..data.size - MIN_DAY_ANALISIS_INICIO) {

            val k = data.size - MIN_DAY_ANALISIS_INICIO - i
            val subData = data.subList(k, data.size)

            if (money > 0) {
                if (data.size > subData.size + MIN_DAY_ANALISIS_FIN) {
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

            if (money == 0.0) {
                daysInverted++
            }
        }

        val rendimiento = stock * data[0].value * CONSTANTE_COMISION + money

        return TestResult(daysInverted, rendimiento, data[0].symbol)

    }

    companion object {

        // No se puede decidir sin historial
        const val MIN_DAY_ANALISIS_INICIO = 70

        // No quiero analizar el coeficiente si no tengo datos suficientes para analizar despues que paso
        const val MIN_DAY_ANALISIS_FIN = 60

        const val CONSTANTE_COMISION: Double = 0.98 // 1,5% de comision y 0,5% de spreed

    }

}