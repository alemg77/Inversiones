package com.a6.inversiones.data.models

import com.a6.inversiones.data.database.StockValue
import com.google.gson.annotations.SerializedName
import java.time.LocalDate

data class AnalisisStockValue(
    @SerializedName("date") val date: Int,
    @SerializedName("value") val value: Double,
    @SerializedName("symbol") val symbol: String,
) {

    constructor(analisisStockValue: AnalisisStockValue) : this(
        analisisStockValue.date,
        analisisStockValue.value,
        analisisStockValue.symbol
    )

    constructor(stockValue: StockValue) : this(
        ((LocalDate.parse(stockValue.date).year - 2000) * 365 + LocalDate.parse(stockValue.date).dayOfYear),
        stockValue.value,
        stockValue.symbol
    )

    constructor(date: String, value: Double, symbol: String) : this(
        ((LocalDate.parse(date).year - 2000) * 365 + LocalDate.parse(date).dayOfYear),
        value,
        symbol
    )


}
