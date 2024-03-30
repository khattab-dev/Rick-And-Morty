package com.slayer.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedPreferenceModule {
    private const val USER_PREFERENCES = "user_data"

    @Singleton
    @Provides
    fun provideSharedPreferencesInstance(@ApplicationContext appContext: Context): SharedPreferences {
        return appContext.getSharedPreferences(USER_PREFERENCES,Context.MODE_PRIVATE)
    }
}