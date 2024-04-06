package com.slayer.data.dataSources.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.slayer.data.dataSources.local.dao.CharacterFavoriteDao
import com.slayer.data.dataSources.local.entities.CharacterEntity

@Database(entities = [CharacterEntity::class], version = 1, exportSchema = false)
abstract class FavoriteDatabase : RoomDatabase() {
    abstract fun getCharacterFavoriteDao() : CharacterFavoriteDao
}