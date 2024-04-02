package com.slayer.domain.usecases

import com.slayer.domain.models.Character
import com.slayer.domain.repositories.CharactersRepository
import javax.inject.Inject

class InsertCharacterToFavUseCase @Inject constructor(
    private val charactersRepository: CharactersRepository
) {
    suspend operator fun invoke(character: Character) = charactersRepository.insertCharacter(character)
}