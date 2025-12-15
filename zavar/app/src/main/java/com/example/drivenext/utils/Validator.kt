// utils/Validator.kt
package com.example.drivenext.utils

import android.util.Patterns
import java.text.SimpleDateFormat
import java.util.*

object Validator {

    // ========== EMAIL ВАЛИДАЦИЯ ==========
    fun isValidEmail(email: String): Boolean {
        if (email.isEmpty()) return false
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun getEmailError(email: String): String? {
        return when {
            email.isEmpty() -> "Введите email"
            !isValidEmail(email) -> "Введите корректный адрес электронной почты"
            else -> null
        }
    }

    // ========== ПАРОЛЬ ВАЛИДАЦИЯ ==========
    fun isValidPassword(password: String): Boolean {
        if (password.length < 6) return false
        return password.any { it.isDigit() } && password.any { it.isLetter() }
    }

    fun getPasswordError(password: String): String? {
        return when {
            password.isEmpty() -> "Введите пароль"
            password.length < 6 -> "Пароль должен быть не менее 6 символов"
            !password.any { it.isDigit() } -> "Пароль должен содержать цифры"
            !password.any { it.isLetter() } -> "Пароль должен содержать буквы"
            else -> null
        }
    }

    fun doPasswordsMatch(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }

    // ========== ИМЯ/ФАМИЛИЯ ВАЛИДАЦИЯ ==========
    fun isValidName(name: String): Boolean {
        return name.length >= 2 && name.all { it.isLetter() }
    }

    fun getNameError(name: String, fieldName: String): String? {
        return when {
            name.isEmpty() -> "Введите $fieldName"
            name.length < 2 -> "$fieldName должен быть не менее 2 символов"
            !name.all { it.isLetter() } -> "$fieldName должен содержать только буквы"
            else -> null
        }
    }

    // ========== ДАТА ВАЛИДАЦИЯ ==========
    fun isValidDate(date: String): Boolean {
        return try {
            val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            dateFormat.isLenient = false
            dateFormat.parse(date)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getDateError(date: String, fieldName: String): String? {
        return when {
            date.isEmpty() -> "Введите $fieldName"
            !isValidDate(date) -> "Неверный формат даты (ДД.ММ.ГГГГ)"
            else -> null
        }
    }
}