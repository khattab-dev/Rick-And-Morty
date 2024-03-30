package com.slayer.data.repositories

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.slayer.domain.models.NetworkResult
import com.slayer.domain.repositories.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepoImpl @Inject constructor(private val firebaseAuth: FirebaseAuth) : AuthRepository {
    val TAG: String = this.javaClass.simpleName

    override suspend fun login(email: String, password: String): NetworkResult<AuthResult> {
        return withContext(Dispatchers.IO) {
            try {
                val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
                NetworkResult.Success(200, result)
            } catch (e: Exception) {
                NetworkResult.Error(401, e.message,e)
            }
        }
    }

    override suspend fun loginWithGoogle(token: String): NetworkResult<AuthResult> {
        return withContext(Dispatchers.IO) {
            try {
                val credential = GoogleAuthProvider.getCredential(token, null)
                val result = firebaseAuth.signInWithCredential(credential).await()

                NetworkResult.Success(200, result)
            } catch (e: Exception) {
                NetworkResult.Error(401, e.message,e)
            }
        }
    }

    override suspend fun register(email: String, password: String): NetworkResult<AuthResult> {
        return withContext(Dispatchers.IO) {
            try {
                val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
                NetworkResult.Success(200, result)
            } catch (e: Exception) {
                NetworkResult.Error(401, e.message,e)
            }
        }
    }

    override suspend fun forgetPassword(email: String): NetworkResult<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                firebaseAuth.sendPasswordResetEmail(email).await()
                NetworkResult.Success(200,null)
            } catch (e: Exception) {
                NetworkResult.Error(401, e.message,e)
            }
        }
    }

    override fun logout(): Flow<NetworkResult<Boolean>> {
        return callbackFlow {
            firebaseAuth.signOut()

            if (firebaseAuth.currentUser == null) {
                trySend(NetworkResult.Success(200,true))
            } else {
                trySend(NetworkResult.Success(200,false))
            }
        }
    }
}