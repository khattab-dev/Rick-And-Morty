package com.slayer.domain.repositories

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCharacterUseCase @Inject constructor(
    private val repo : CharactersRepository
) {
    operator fun invoke() : Flow<Any> {
       return repo.getCharacters()
    }
}