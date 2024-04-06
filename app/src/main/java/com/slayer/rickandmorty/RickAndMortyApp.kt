package com.slayer.rickandmorty

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.slayer.common.Constants
import com.slayer.data.workers.SyncFavoriteWorker
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class RickAndMortyApp(
) : Application(), Configuration.Provider {
    // @Inject lateinit var workerFactory: HiltWorkerFactory
    // https://github.com/google/dagger/issues/2766#issuecomment-886195011
    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface HiltWorkerFactoryEntryPoint {
        fun workerFactory(): HiltWorkerFactory
    }

    override val workManagerConfiguration: Configuration =
        Configuration.Builder()
            .setWorkerFactory(
                EntryPoints.get(this, HiltWorkerFactoryEntryPoint::class.java).workerFactory()
            ).build()

    override fun onCreate() {
        super.onCreate()

        val workerConstraints =
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        val workerRequest = PeriodicWorkRequestBuilder<SyncFavoriteWorker>(
            repeatInterval = 1,
            TimeUnit.DAYS
        ).setConstraints(workerConstraints).build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            Constants.FAVORITE_SYNC_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            workerRequest
        )
    }
}