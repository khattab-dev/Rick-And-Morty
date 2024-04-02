package com.slayer.di

import android.content.Context
import androidx.room.Room
import com.slayer.data.source.local.FavoriteDatabase
import com.slayer.data.source.local.dao.CharacterFavoriteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    @Provides
    @Singleton
    fun provideListDao(trackDatabase: FavoriteDatabase): CharacterFavoriteDao {
        return trackDatabase.getCharacterFavoriteDao()
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): FavoriteDatabase {
        return Room.databaseBuilder(
            context,
            FavoriteDatabase::class.java,
            "favorite_database"
        ).build()
    }
}