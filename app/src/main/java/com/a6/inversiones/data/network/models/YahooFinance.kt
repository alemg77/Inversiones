package com.a6.inversiones.data.network.models


import com.a6.inversiones.data.network.GenericApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

class YahooFinance {

    private val loggingInterceptor = run {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.apply {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private val okHttpClient = OkHttpClient()
        .newBuilder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .build()


    private val buildRetrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .client(okHttpClient)
        .build()

    private val api = buildRetrofit.create(GenericApi::class.java)

    suspend fun getGeneric(url: String): String? {
        return try {
            api.genericGet(url).body()
        } catch (e: Exception) {
            "null"
        }
    }

    companion object {
        private const val BASE_URL = "https://finance.yahoo.com/quote/"
        private const val API_KEY = "52633eabf9ae27d5be31aecdd2867ab2"
    }

}