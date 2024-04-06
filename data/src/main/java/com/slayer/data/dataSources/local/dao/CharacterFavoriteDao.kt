package com.slayer.data.dataSources.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.slayer.data.dataSources.local.entities.CharacterEntity

@Dao
interface CharacterFavoriteDao {
    @Query("SELECT COUNT(*) FROM characterentity WHERE id = :characterId")
    suspend fun isCharacterExist(characterId: Int): Int

    @Query("SELECT id FROM characterentity")
    fun getAllFavoriteCharacters() : List<Int>

    @Query("DELETE FROM characterentity")
    suspend fun clearCharacterFavorite()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(character : CharacterEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(character : List<CharacterEntity>)

    @Delete
    suspend fun deleteCharacter(character : CharacterEntity)
}