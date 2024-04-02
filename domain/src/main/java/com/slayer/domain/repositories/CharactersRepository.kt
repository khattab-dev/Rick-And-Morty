package com.slayer.domain.repositories

import com.slayer.domain.models.Character

interface CharactersRepository {
    suspend fun doesExistInFavorite(id : Int) : Boolean
    suspend fun insertCharacter(character: Character)
    suspend fun deleteCharacter(character: Character)
}