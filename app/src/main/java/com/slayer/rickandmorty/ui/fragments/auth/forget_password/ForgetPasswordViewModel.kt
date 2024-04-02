package com.slayer.rickandmorty.ui.fragments.auth.forget_password

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.slayer.domain.models.NetworkResult
import com.slayer.domain.usecases.ForgetPasswordUseCase
import com.slayer.rickandmorty.R
import com.slayer.rickandmorty.core.printToLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ForgetPasswordViewModel @Inject constructor(
    private val forgetPasswordUseCase: ForgetPasswordUseCase
) : ViewModel() {
    private val _forgetPasswordResult = MutableStateFlow<Boolean?>(null)
    val forgetPasswordResult = _forgetPasswordResult.asStateFlow()

    private val _authErrorFlow = MutableStateFlow<Exception?>(null)
    private val authErrorFlow = _authErrorFlow.asStateFlow()

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading = _isLoading.asStateFlow()

    fun setLoadingValue(value : Boolean) {
        _isLoading.value = value
    }

    suspend fun forgetPassword(email : String) = forgetPasswordUseCase(email).apply {
        when (this) {
            is NetworkResult.Error -> {
                _forgetPasswordResult.value = false
                _authErrorFlow.value = this.exception
            }

            is NetworkResult.Exception -> {
                this.e.stackTraceToString().printToLog()
            }

            is NetworkResult.Success -> _forgetPasswordResult.value = true
        }
    }

    fun handleForgetPasswordExceptions(): Int {
        return when (val exception = authErrorFlow.value) {
            is FirebaseAuthInvalidUserException -> {
                R.string.email_is_not_registered
            }

            else -> {
                exception?.stackTraceToString().printToLog()
                R.string.something_went_wrong_auth
            }
        }
    }
}