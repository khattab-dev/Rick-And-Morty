package com.slayer.data.source.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.slayer.data.source.local.entities.CharacterEntity

@Dao
interface CharacterFavoriteDao {
    @Query("SELECT COUNT(*) FROM characterentity WHERE id = :characterId")
    suspend fun isCharacterExist(characterId: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(character : CharacterEntity)

    @Delete
    suspend fun deleteCharacter(character : CharacterEntity)
}