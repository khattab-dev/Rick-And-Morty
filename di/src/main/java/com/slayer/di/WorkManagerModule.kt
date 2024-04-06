package com.slayer.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object WorkManagerModule {
//    fun provideSyncFavoriteWorker(@ApplicationContext context: Context,params: WorkerParameters) : SyncFavoriteWorker {
//        return SyncFavoriteWorker(context,params)
//    }
}