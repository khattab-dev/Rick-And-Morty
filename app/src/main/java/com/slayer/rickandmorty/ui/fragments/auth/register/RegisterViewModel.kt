package com.slayer.rickandmorty.ui.fragments.auth.register

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.slayer.common.printToLog
import com.slayer.domain.models.NetworkResult
import com.slayer.domain.usecases.auth.GoogleAuthUseCase
import com.slayer.domain.usecases.auth.RegisterUseCase
import com.slayer.rickandmorty.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val googleAuthUseCase: GoogleAuthUseCase
) : ViewModel() {
    private val _registerResult = MutableStateFlow<AuthResult?>(null)
    val registerResult = _registerResult.asStateFlow()

    private val _authErrorFlow = MutableStateFlow<Exception?>(null)
    private val authErrorFlow = _authErrorFlow.asStateFlow()

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading = _isLoading.asStateFlow()

    fun setLoadingValue(value : Boolean) {
        _isLoading.value = value
    }

    suspend fun tryRegister(email: String, password: String) {
        registerUseCase(email, password).apply {
            when (this) {
                is NetworkResult.Error -> {
                    _registerResult.value = null
                    _authErrorFlow.value = this.exception
                }
                is NetworkResult.Exception -> {
                    this.e.stackTraceToString().printToLog()
                }
                is NetworkResult.Success -> _registerResult.value = this.data as AuthResult
            }
        }
    }

    suspend fun tryLoginWithGoogle(token : String) {
        googleAuthUseCase(token).apply {
            when (this) {
                is NetworkResult.Error -> {
                    _registerResult.value = null
                    _authErrorFlow.value = this.exception
                }
                is NetworkResult.Exception -> {
                    this.e.stackTraceToString().printToLog()
                }
                is NetworkResult.Success -> _registerResult.value = this.data as AuthResult
            }
        }
    }

    fun handleSignUpWithEmailAndPasswordException(): Int {
        return when (val exception = authErrorFlow.value) {
            is FirebaseAuthUserCollisionException -> {
                R.string.account_already_registered
            }

            else -> {
                exception?.stackTraceToString().printToLog()
                R.string.something_went_wrong_auth
            }
        }
    }
}