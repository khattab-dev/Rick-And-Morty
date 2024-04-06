package com.slayer.domain.usecases.characters

import com.slayer.domain.repositories.CharactersNetworkRepository
import javax.inject.Inject

class GetRandomCharacters @Inject constructor(private val repo : CharactersNetworkRepository) {
     suspend operator fun invoke(ids : String) = repo.getRandomChars(ids)
}