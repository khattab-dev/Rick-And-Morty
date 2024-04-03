package com.slayer.domain.repositories

import com.slayer.domain.models.NetworkResult

interface CharactersNetworkRepository {
    suspend fun getRandomChars(string: String) : NetworkResult<Any>
}