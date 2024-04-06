package com.slayer.domain.usecases.characters

import com.slayer.domain.repositories.CharactersLocalRepository
import javax.inject.Inject

class ClearCharacterFavoriteUseCase @Inject constructor(
    private val repository: CharactersLocalRepository
) {
    suspend operator  fun invoke() = repository.clearCharacters()
}