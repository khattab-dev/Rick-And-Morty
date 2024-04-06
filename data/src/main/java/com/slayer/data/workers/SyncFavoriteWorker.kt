package com.slayer.data.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.slayer.common.Constants
import com.slayer.common.printToLog
import com.slayer.domain.usecases.characters.GetFavoriteCharactersIds
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncFavoriteWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val getFavoriteCharactersIds: GetFavoriteCharactersIds,
) : CoroutineWorker(appContext, workerParams) {
    private val TAG = this.javaClass.simpleName

    override suspend fun doWork(): Result {
        return syncLocalFavoriteWithCloud()
    }

    private fun syncLocalFavoriteWithCloud(): Result {
        val db = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()

        return try {
            val favoriteCharacters = hashMapOf(
                Constants.KEY_FAVORITE_CHARS_IDS to getFavoriteCharactersIds.invoke(),
            )
            if (auth.currentUser != null) {
                auth.currentUser?.let { user ->
                    db.collection(
                        Constants.COLLECTION_PATH_FAV_CHARS
                    ).document(user.uid).set(favoriteCharacters).addOnSuccessListener {
                        "Success".printToLog(TAG)
                    }.addOnFailureListener {
                        it.stackTraceToString().printToLog(TAG)
                    }
                }
                Result.success()
            } else {
                Result.retry()
            }
        } catch (e: Exception) {
            e.stackTraceToString().printToLog(TAG)
            Result.failure()
        }
    }
}
