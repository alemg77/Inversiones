package com.a6.inversiones.data.analysis

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


    companion object {
        const val CAIDA_COMPRA_PORCIENTO = 15.1
    }

}