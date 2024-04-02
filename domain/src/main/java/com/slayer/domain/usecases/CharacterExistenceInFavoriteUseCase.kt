package com.slayer.domain.usecases

import com.slayer.domain.repositories.CharactersRepository
import javax.inject.Inject

class CharacterExistenceInFavoriteUseCase @Inject constructor(
    private val charactersRepository : CharactersRepository
) {
   suspend operator fun invoke(id : Int) = charactersRepository.doesExistInFavorite(id)
}