package com.slayer.data.repositories

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.slayer.common.Constants
import com.slayer.domain.models.Character
import com.slayer.domain.models.NetworkResult
import com.slayer.domain.repositories.AuthRepository
import com.slayer.domain.usecases.characters.ClearCharacterFavoriteUseCase
import com.slayer.domain.usecases.characters.InsertCharacterToFavUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class AuthRepoImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val clearCharacterFavoriteUseCase: ClearCharacterFavoriteUseCase,
    private val insertCharacterToFavUseCase: InsertCharacterToFavUseCase,
    private val fireStore: FirebaseFirestore
) : AuthRepository {
    val TAG: String = this.javaClass.simpleName

    override suspend fun login(email: String, password: String): NetworkResult<AuthResult> {
        return withContext(Dispatchers.IO) {
            try {
                val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()

                fetchAndCacheUserCloudData()

                NetworkResult.Success(200, result)
            } catch (e: Exception) {
                NetworkResult.Error(401, e.message, e)
            }
        }
    }

    override suspend fun loginWithGoogle(token: String): NetworkResult<AuthResult> {
        return withContext(Dispatchers.IO) {
            try {
                val credential = GoogleAuthProvider.getCredential(token, null)
                val result = firebaseAuth.signInWithCredential(credential).await()

                fetchAndCacheUserCloudData()

                NetworkResult.Success(200, result)
            } catch (e: Exception) {
                NetworkResult.Error(401, e.message, e)
            }
        }
    }

    override suspend fun register(email: String, password: String): NetworkResult<AuthResult> {
        return withContext(Dispatchers.IO) {
            try {
                val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                NetworkResult.Success(200, result)
            } catch (e: Exception) {
                NetworkResult.Error(401, e.message, e)
            }
        }
    }

    override suspend fun forgetPassword(email: String): NetworkResult<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                firebaseAuth.sendPasswordResetEmail(email).await()
                NetworkResult.Success(200, null)
            } catch (e: Exception) {
                NetworkResult.Error(401, e.message, e)
            }
        }
    }

    override suspend fun logout(): Boolean {
        clearCharacterFavoriteUseCase.invoke()

        firebaseAuth.signOut()

        return firebaseAuth.currentUser == null
    }

    private suspend fun fetchAndCacheUserCloudData() {
        val document = fireStore.collection(Constants.COLLECTION_PATH_FAV_CHARS)
            .document(firebaseAuth.currentUser!!.uid).get().await()
        val data = document.data

        val ids = data?.get(Constants.KEY_FAVORITE_CHARS_IDS) as List<Int>

        insertCharacterToFavUseCase(
            ids.map { Character(it) }
        )
    }
}
