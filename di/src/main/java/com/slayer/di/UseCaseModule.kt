package com.slayer.di

import com.slayer.domain.repositories.AuthRepository
import com.slayer.domain.repositories.CharactersRepository
import com.slayer.domain.repositories.GetCharacterUseCase
import com.slayer.domain.usecases.GoogleAuthUseCase
import com.slayer.domain.usecases.LoginUseCase
import com.slayer.domain.usecases.LogoutUseCase
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

    @Provides
    @Singleton
    fun provideLogoutUseCase(
        repository: AuthRepository
    ) : LogoutUseCase {
        return LogoutUseCase(repository)
    }
}