package com.slayer.domain.usecases.auth

import com.slayer.domain.repositories.AuthRepository
import javax.inject.Inject

class GoogleAuthUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(token : String) = authRepository.loginWithGoogle(token)
}