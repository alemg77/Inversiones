package com.a6.inversiones.data.models

import com.google.gson.annotations.SerializedName

data class AnalisisStockValue(
    @SerializedName("date") val date: Int,
    @SerializedName("value") val value: Double,
    @SerializedName("symbol") val symbol: String,
)
