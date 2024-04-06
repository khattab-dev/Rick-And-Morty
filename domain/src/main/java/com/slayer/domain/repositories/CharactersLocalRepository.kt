package com.slayer.domain.repositories

import com.slayer.domain.models.Character

interface CharactersLocalRepository {
    fun getAllFavoriteCharacters() : List<Int>
    suspend fun doesExistInFavorite(id : Int) : Boolean
    suspend fun insertCharacter(character: Character)
    suspend fun insertCharacter(character: List<Character>)
    suspend fun deleteCharacter(character: Character)

    suspend fun clearCharacters()
}