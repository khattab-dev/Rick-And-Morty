package com.slayer.data.repositories

import com.slayer.data.source.local.dao.CharacterFavoriteDao
import com.slayer.data.source.local.entities.CharacterEntity
import com.slayer.domain.models.Character
import com.slayer.domain.repositories.CharactersLocalRepository
import javax.inject.Inject

class CharactersLocalRepoImpl @Inject constructor(
    private val characterFavoriteDao: CharacterFavoriteDao
) : CharactersLocalRepository {
    override suspend fun doesExistInFavorite(id: Int): Boolean {
        return characterFavoriteDao.isCharacterExist(id) == 1
    }

    override suspend fun insertCharacter(character: Character) {
        characterFavoriteDao.insertCharacter(CharacterEntity(character.id))
    }

    override suspend fun deleteCharacter(character: Character) {
        characterFavoriteDao.deleteCharacter(CharacterEntity(character.id))
    }
}