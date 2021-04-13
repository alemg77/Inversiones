package com.a6.inversiones.data.network.models


import com.google.gson.annotations.SerializedName

data class EndOfDayResponse(
    @SerializedName("data") val `data`: List<DataEndOfDayResponse>,
    @SerializedName("pagination") val pagination: Pagination
)