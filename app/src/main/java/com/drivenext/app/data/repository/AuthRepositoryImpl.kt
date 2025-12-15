package com.drivenext.app.data.repository

import com.drivenext.app.domain.model.User
import com.drivenext.app.utils.Prefs
import kotlinx.coroutines.delay

/**
 * Реализация репозитория аутентификации
 * TODO: Интегрировать с Supabase
 */
class AuthRepositoryImpl(
    private val prefs: Prefs
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<String> {
        // TODO: Реализовать через Supabase
        delay(1000) // Имитация запроса
        return if (email.isNotEmpty() && password.isNotEmpty()) {
            val token = "mock_token_${System.currentTimeMillis()}"
            prefs.accessToken = token
            prefs.isLoggedIn = true
            prefs.userEmail = email
            Result.success(token)
        } else {
            Result.failure(Exception("Неверный email или пароль"))
        }
    }

    override suspend fun loginWithGoogle(idToken: String): Result<String> {
        // TODO: Реализовать через Supabase
        delay(1000)
        val token = "mock_google_token_${System.currentTimeMillis()}"
        prefs.accessToken = token
        prefs.isLoggedIn = true
        return Result.success(token)
    }

    override suspend fun register(email: String, password: String): Result<String> {
        // TODO: Реализовать через Supabase
        delay(1000)
        val token = "mock_register_token_${System.currentTimeMillis()}"
        prefs.accessToken = token
        prefs.isLoggedIn = true
        prefs.userEmail = email
        return Result.success(token)
    }

    override suspend fun getCurrentUser(): Result<User?> {
        // TODO: Реализовать через Supabase
        val email = prefs.userEmail ?: return Result.success(null)
        return Result.success(
            User(
                id = prefs.userId ?: "1",
                email = email
            )
        )
    }

    override suspend fun logout(): Result<Unit> {
        prefs.clear()
        return Result.success(Unit)
    }

    override fun isLoggedIn(): Boolean {
        return prefs.isLoggedIn && prefs.accessToken != null
    }
}

