package com.slayer.domain.repositories

import com.slayer.domain.models.NetworkResult

interface AuthRepository {
    suspend fun login(email: String, password: String): NetworkResult<Any>
    suspend fun loginWithGoogle(token: String): NetworkResult<Any>
    suspend fun register(email: String, password: String): NetworkResult<Any>
    suspend fun forgetPassword(email: String): NetworkResult<Any>
    fun logout(): Boolean
}