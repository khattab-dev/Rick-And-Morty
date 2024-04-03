package com.slayer.data.repositories

import com.slayer.data.source.api.ApiHandler
import com.slayer.data.source.api.ApiService
import com.slayer.data.source.api.dto.characters.CharacterResult.Companion.toCharacter
import com.slayer.domain.models.Character
import com.slayer.domain.models.NetworkResult
import com.slayer.domain.repositories.CharactersNetworkRepository
import javax.inject.Inject

class CharactersNetworkRepoImpl @Inject constructor(private val api : ApiService) : CharactersNetworkRepository,ApiHandler {
    override suspend fun getRandomChars(ids: String): NetworkResult<List<Character>> {
        return handleApi(
            execute = { api.getRandomCharacters(ids) },
            mapper = { data ->
                data.map { it.toCharacter(false) }
            }
        )
    }
}