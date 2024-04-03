package com.slayer.domain.repositories

import com.slayer.domain.models.Character

interface CharactersLocalRepository {
    suspend fun doesExistInFavorite(id : Int) : Boolean
    suspend fun insertCharacter(character: Character)
    suspend fun deleteCharacter(character: Character)
}