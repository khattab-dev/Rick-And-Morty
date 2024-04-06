package com.slayer.data.work_managers

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.FirebaseFirestore

class SyncFavoriteWorker(
    appContext: Context,
    workerParams: WorkerParameters,

) : Worker(appContext, workerParams) {

    override fun doWork(): Result {

        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        try {
            val user = hashMapOf(
                "first" to "Ada",
                "last" to "Lovelace",
                "born" to 1815
            )

            db.collection("users")
                .add(user)
                .addOnSuccessListener { documentReference ->
                    Log.d("rabbit", "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w("rabbit", "Error adding document", e)
                }
            return Result.success()
        } catch (e : Exception) {
            Log.d("rabbit", "doWork: $e")
            return Result.failure()
        }
    }
}