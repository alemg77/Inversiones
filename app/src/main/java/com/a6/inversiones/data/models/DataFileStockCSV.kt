package com.a6.inversiones.data.models

data class DataFileStockCSV(
    val date: String,
    val high: Double,
    val low: Double,
    val open: Double,
    val close: Double,
    val volume: Double,
    val adj: Double
)
