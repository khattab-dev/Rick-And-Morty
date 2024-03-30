package com.slayer.domain.usecases

import com.slayer.domain.repositories.AuthRepository
import javax.inject.Inject

class LogoutUseCase @Inject constructor(private val authRepository: AuthRepository) {
    operator fun invoke() = authRepository.logout()
}