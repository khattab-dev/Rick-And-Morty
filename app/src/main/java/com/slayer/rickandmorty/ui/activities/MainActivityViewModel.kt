package com.slayer.rickandmorty.ui.activities

import androidx.lifecycle.ViewModel
import com.slayer.domain.repositories.SharedPreferenceRepository
import com.slayer.domain.usecases.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val sharedPreferenceRepository: SharedPreferenceRepository,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {
    fun tryLogout() = logoutUseCase.invoke()

    fun isDarkMode() = sharedPreferenceRepository.getDarkModeValue()

    fun setDarkModeValue(value : Boolean) = sharedPreferenceRepository.setDarkModeValue(value)
}