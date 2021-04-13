package com.a6.inversiones.data.network

import com.a6.inversiones.data.network.models.DataResult
import com.a6.inversiones.data.network.models.toErrorResult
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MarketStackRetrofitBuilder {

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

    private val buildAuthRetrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    private val api = buildAuthRetrofit.create(ApiMarketStack::class.java)

    suspend fun getEndOfDay(symbols: String): DataResult<out Any> {
        return try {
            val fetchEndOfDay = api.fetchEndOfDay(API_KEY, symbols)
            DataResult.Success(fetchEndOfDay)
        } catch (e: Throwable) {
            DataResult.Error(e.toErrorResult())
        }
    }

    suspend fun getEndOfDay(
        symbols: String,
        dateFrom: String,
        dateTo: String
    ): DataResult<out Any> {
        return try {
            val fetchEndOfDay = api.fetchEndOfDay(API_KEY, symbols, dateFrom, dateTo)
            DataResult.Success(fetchEndOfDay)
        } catch (e: Throwable) {
            DataResult.Error(e.toErrorResult())
        }
    }

    companion object {
        private const val BASE_URL = "http://api.marketstack.com/v1/"
        private const val API_KEY = "52633eabf9ae27d5be31aecdd2867ab2"
    }


}