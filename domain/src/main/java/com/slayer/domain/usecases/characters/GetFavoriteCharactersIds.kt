package com.slayer.domain.usecases.characters

import com.slayer.domain.repositories.CharactersLocalRepository
import javax.inject.Inject

class GetFavoriteCharactersIds @Inject constructor(private val repository: CharactersLocalRepository) {
    operator fun invoke() = repository.getAllFavoriteCharacters()
}