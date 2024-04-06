package com.slayer.domain.usecases.auth

import com.slayer.domain.repositories.AuthRepository
import javax.inject.Inject

class ForgetPasswordUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String) = authRepository.forgetPassword(email)
}