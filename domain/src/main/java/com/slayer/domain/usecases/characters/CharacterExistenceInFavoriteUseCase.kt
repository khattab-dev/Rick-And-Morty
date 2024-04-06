package com.slayer.domain.usecases.characters

import com.slayer.domain.repositories.CharactersLocalRepository
import javax.inject.Inject

class CharacterExistenceInFavoriteUseCase @Inject constructor(
    private val charactersLocalRepository : CharactersLocalRepository
) {
   suspend operator fun invoke(id : Int) = charactersLocalRepository.doesExistInFavorite(id)
}