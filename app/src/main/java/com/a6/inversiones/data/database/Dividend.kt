package com.a6.inversiones.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity
data class Dividend(
    @SerializedName("symbol") val symbol: String,
    @SerializedName("value") val value: Double,
    @SerializedName("date") val date: String,
) {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    constructor(company: Dividend) : this(
        company.symbol,
        company.value,
        company.date
    )

}