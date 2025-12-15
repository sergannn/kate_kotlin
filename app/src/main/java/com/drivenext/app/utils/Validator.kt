package com.drivenext.app.utils

import android.util.Patterns
import java.text.SimpleDateFormat
import java.util.*

/**
 * Утилита для валидации данных формы
 */
object Validator {

    /**
     * Валидация email
     */
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

    /**
     * Валидация пароля
     */
    fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }

    fun getPasswordError(password: String): String? {
        return when {
            password.isEmpty() -> "Введите пароль"
            password.length < 6 -> "Пароль должен быть не менее 6 символов"
            else -> null
        }
    }

    fun doPasswordsMatch(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }

    /**
     * Валидация имени/фамилии
     */
    fun isValidName(name: String): Boolean {
        return name.length >= 2 && name.all { it.isLetter() || it == '-' || it == ' ' }
    }

    fun getNameError(name: String, fieldName: String): String? {
        return when {
            name.isEmpty() -> "Введите $fieldName"
            name.length < 2 -> "$fieldName должен быть не менее 2 символов"
            !name.all { it.isLetter() || it == '-' || it == ' ' } -> "$fieldName должен содержать только буквы"
            else -> null
        }
    }

    /**
     * Валидация даты (формат DD/MM/YYYY)
     */
    fun isValidDate(date: String, format: String = "dd/MM/yyyy"): Boolean {
        return try {
            val dateFormat = SimpleDateFormat(format, Locale.getDefault())
            dateFormat.isLenient = false
            dateFormat.parse(date) != null
        } catch (e: Exception) {
            false
        }
    }

    fun getDateError(date: String, fieldName: String): String? {
        return when {
            date.isEmpty() -> "Введите $fieldName"
            !isValidDate(date) -> "Неверный формат даты (ДД/ММ/ГГГГ)"
            else -> null
        }
    }
}

