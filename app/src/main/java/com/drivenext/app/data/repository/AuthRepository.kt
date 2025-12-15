package com.drivenext.app.data.repository

import com.drivenext.app.domain.model.User

/**
 * Репозиторий для работы с аутентификацией
 */
interface AuthRepository {
    suspend fun login(email: String, password: String): Result<String> // Возвращает токен
    suspend fun loginWithGoogle(idToken: String): Result<String>
    suspend fun register(email: String, password: String): Result<String>
    suspend fun getCurrentUser(): Result<User?>
    suspend fun logout(): Result<Unit>
    fun isLoggedIn(): Boolean
}

