package com.slayer.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.slayer.data.source.local.dao.CharacterFavoriteDao
import com.slayer.data.source.local.entities.CharacterEntity

@Database(entities = [CharacterEntity::class], version = 1, exportSchema = false)
abstract class FavoriteDatabase : RoomDatabase() {
    abstract fun getCharacterFavoriteDao() : CharacterFavoriteDao
}