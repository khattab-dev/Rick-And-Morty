package com.slayer.rickandmorty.ui.activities

import androidx.lifecycle.ViewModel
import com.slayer.domain.repositories.SharedPreferenceRepository
import com.slayer.domain.usecases.auth.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val sharedPreferenceRepository: SharedPreferenceRepository,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {
    suspend fun tryLogout() = withContext(Dispatchers.IO) { logoutUseCase.invoke() }

    fun isDarkMode() = sharedPreferenceRepository.getDarkModeValue()

    fun setDarkModeValue(value : Boolean) = sharedPreferenceRepository.setDarkModeValue(value)
}