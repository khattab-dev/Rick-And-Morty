package com.slayer.domain.repositories.repositories

import com.slayer.domain.repositories.models.NetworkResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, password: String): NetworkResult<Any>
    suspend fun loginWithGoogle(token: String): NetworkResult<Any>
    suspend fun register(email: String, password: String): NetworkResult<Any>
    suspend fun forgetPassword(email: String): NetworkResult<Any>
    fun logout(): Flow<NetworkResult<Any>>
}