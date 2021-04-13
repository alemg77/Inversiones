package com.a6.inversiones.data.network.models


import com.google.gson.annotations.SerializedName

data class DataEndOfDayResponse(
    @SerializedName("adj_close")
    val adjClose: Double,
    @SerializedName("adj_high")
    val adjHigh: Any,
    @SerializedName("adj_low")
    val adjLow: Any,
    @SerializedName("adj_open")
    val adjOpen: Any,
    @SerializedName("adj_volume")
    val adjVolume: Any,
    @SerializedName("close")
    val close: Double,
    @SerializedName("date")
    val date: String,
    @SerializedName("exchange")
    val exchange: String,
    @SerializedName("high")
    val high: Double,
    @SerializedName("low")
    val low: Double,
    @SerializedName("open")
    val `open`: Double,
    @SerializedName("split_factor")
    val splitFactor: Double,
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("volume")
    val volume: Double
)