package com.slayer.di

import com.slayer.data.repositories.AuthRepoImpl
import com.slayer.data.repositories.CharactersLocalRepoImpl
import com.slayer.data.repositories.CharactersNetworkRepoImpl
import com.slayer.data.repositories.SharedPrefRepoImpl
import com.slayer.domain.repositories.AuthRepository
import com.slayer.domain.repositories.CharactersLocalRepository
import com.slayer.domain.repositories.CharactersNetworkRepository
import com.slayer.domain.repositories.SharedPreferenceRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindCharactersRepo(
        repoImpl: CharactersLocalRepoImpl
    ) : CharactersLocalRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepo(authRepoImpl: AuthRepoImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindSharedPreferenceRepo(authRepoImpl: SharedPrefRepoImpl): SharedPreferenceRepository

    @Binds
    @Singleton
    abstract fun bindCharactersRepoNetwork(repo: CharactersNetworkRepoImpl): CharactersNetworkRepository
}