package com.slayer.rickandmorty

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.slayer.data.work_managers.SyncFavoriteWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class RickAndMortyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        val workerRequest = PeriodicWorkRequestBuilder<SyncFavoriteWorker>(
            repeatInterval = 15,
            TimeUnit.MINUTES
        ).build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "sync",
            ExistingPeriodicWorkPolicy.UPDATE,
            workerRequest
        )
    }
}