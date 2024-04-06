package com.slayer.data.dataSources.api

import com.slayer.domain.models.NetworkResult
import retrofit2.HttpException
import retrofit2.Response

interface ApiHandler {
    suspend fun <T : Any, R : Any> handleApi(
        execute: suspend () -> Response<T>,
        mapper: (T) -> R,
    ): NetworkResult<R> {
        return try {
            val response = execute()
            val body = response.body()

            if (response.isSuccessful && body != null) {
                val mappedResult = mapper(body)
                NetworkResult.Success(response.code(), mappedResult)
            } else {
                NetworkResult.Error(
                    code = response.code(),
                    errorMsg = response.errorBody()?.string() ?: "Unknown error"
                )
            }
        } catch (e: HttpException) {
            NetworkResult.Error(e.code(), e.message())
        } catch (e: Throwable) {
            NetworkResult.Exception(e)
        }
    }
}