package com.slayer.rickandmorty.ui.activities

import androidx.lifecycle.ViewModel
import com.slayer.domain.usecases.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {
    fun tryLogout() = logoutUseCase.invoke()
}