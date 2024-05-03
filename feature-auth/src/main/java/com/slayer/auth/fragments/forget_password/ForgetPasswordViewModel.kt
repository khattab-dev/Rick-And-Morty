package com.slayer.login.fragments.forget_password

import androidx.lifecycle.ViewModel
import com.slayer.common.printToLog
import com.slayer.domain.models.NetworkResult
import com.slayer.domain.usecases.auth.ForgetPasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ForgetPasswordViewModel @Inject constructor(
    private val forgetPasswordUseCase: ForgetPasswordUseCase
) : ViewModel() {
    private val TAG = this.javaClass.simpleName

    private val _forgetPasswordResult = MutableStateFlow<Boolean?>(null)
    val forgetPasswordResult = _forgetPasswordResult.asStateFlow()

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading = _isLoading.asStateFlow()

    fun setLoadingValue(value : Boolean) {
        _isLoading.value = value
    }

    suspend fun forgetPassword(email : String) = forgetPasswordUseCase(email).apply {
        when (this) {
            is NetworkResult.Error -> {
                _forgetPasswordResult.value = false
                this.errorMsg.printToLog(TAG)
            }

            is NetworkResult.Exception -> {
                this.e.stackTraceToString().printToLog(TAG)
            }

            is NetworkResult.Success -> _forgetPasswordResult.value = true
        }
    }
}