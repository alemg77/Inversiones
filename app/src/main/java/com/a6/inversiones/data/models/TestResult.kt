package com.a6.inversiones.data.models

import kotlin.math.pow

data class TestResult(
    val daysInvested: Int,
    val result: Double,
    val symbol: String,
    val timesInverted: Int
) {

    fun rendimientoAnual(): Double {
        val years = this.daysInvested.toDouble() / 252.0
        val r = this.result / 100
        return r.pow(1.0 / years) * timesInverted.toDouble().pow(0.5)
    }
}