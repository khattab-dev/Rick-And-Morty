package com.slayer.domain.repositories.models

import java.lang.Exception

sealed class NetworkResult<out T : Any> {
    class Success<T : Any>(val code: Int, val data: T?) : NetworkResult<T>()
    class Error<T : Any>(
        val code: Int,
        val errorMsg: String?,
        val exception: java.lang.Exception? = null
    ) : NetworkResult<T>()
    class Exception<T : Any>(val e: Throwable) : NetworkResult<T>()
}