package com.slayer.di

import com.slayer.data.ApiService
import com.slayer.data.repositories.CharactersRepoImpl
import com.slayer.domain.repositories.GetCharacterUseCase
import com.slayer.domain.repositories.repositories.AuthRepository
import com.slayer.domain.repositories.repositories.CharactersRepository
import com.slayer.domain.usecases.GoogleAuthUseCase
import com.slayer.domain.usecases.LoginUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideGetCustomersUseCase(
        repository: CharactersRepository
    ) : GetCharacterUseCase {
        return GetCharacterUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGoogleAuthUseCase(
        repository: AuthRepository
    ) : GoogleAuthUseCase {
        return GoogleAuthUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideLoginUseCase(
        repository: AuthRepository
    ) : LoginUseCase {
        return LoginUseCase(repository)
    }
}