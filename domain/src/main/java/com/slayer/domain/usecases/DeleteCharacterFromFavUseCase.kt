package com.slayer.domain.usecases

import com.slayer.domain.models.Character
import com.slayer.domain.repositories.CharactersLocalRepository
import javax.inject.Inject

class DeleteCharacterFromFavUseCase @Inject constructor(
    private val charactersLocalRepository: CharactersLocalRepository
) {
    suspend operator fun invoke(character: Character) = charactersLocalRepository.deleteCharacter(character)
}