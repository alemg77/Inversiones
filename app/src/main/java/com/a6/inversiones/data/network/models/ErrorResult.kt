package com.a6.inversiones.data.network.models

import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException

sealed class ErrorResult {

    data class HttpException(val data: Throwable) : ErrorResult()
    data class IOException(val data: Throwable) : ErrorResult()
    data class Unknown(val data: Throwable) : ErrorResult()
}

fun Throwable.toErrorResult(): ErrorResult {
    return when (this) {
        is IOException -> {
            if (this is UnknownHostException) {
                // If this is UnknownHostException, retrofit will re cast to HttpException
                ErrorResult.HttpException(this)
            } else {
                ErrorResult.IOException(this)
            }
        }
        is HttpException -> {
            ErrorResult.HttpException(this)
        }
        else -> {
            ErrorResult.Unknown(this)
        }
    }
}
