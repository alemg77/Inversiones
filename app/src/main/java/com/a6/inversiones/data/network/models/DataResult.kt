package com.a6.inversiones.data.network.models

sealed class DataResult<T> {

    data class Success<T>(val data: T) : DataResult<T>()

    data class Error<T>(val error: T) : DataResult<T>()

}
