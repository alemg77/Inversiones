package com.a6.inversiones.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface GenericApi {


    @GET
    suspend fun genericGet(
        @Url url: String
    ): Response<String>


}