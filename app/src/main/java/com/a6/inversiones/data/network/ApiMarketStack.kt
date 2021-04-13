package com.a6.inversiones.data.network

import com.a6.inversiones.data.network.models.EndOfDayResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiMarketStack {

    @GET(END_OF_DAY)
    suspend fun fetchEndOfDay(
        @Query(ACCESS_KEY) accessKey: String,
        @Query(SYMBOLS) symbols: String
    ): EndOfDayResponse

    @GET(END_OF_DAY)
    suspend fun fetchEndOfDay(
        @Query(ACCESS_KEY) accessKey: String,
        @Query(SYMBOLS) symbols: String,
        @Query(DATE_FROM) dateFrom: String,
        @Query(DATE_TO) dateTo: String
    ): EndOfDayResponse

    companion object {
        private const val SYMBOLS = "symbols"
        private const val ACCESS_KEY = "access_key"
        private const val END_OF_DAY = "eod"
        private const val DATE_FROM = "date_from"
        private const val DATE_TO = "date_to"
    }

}