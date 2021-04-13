package com.a6.inversiones.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class StockData(
    @SerializedName("date") val date: String,
    @SerializedName("value") val value: Double,
    @SerializedName("symbol") val symbol: String,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    constructor(stockData: StockData) : this(
        stockData.date,
        stockData.value,
        stockData.symbol
    )

}
