package com.example.drivenext.utils

import android.content.Context
import android.content.SharedPreferences

// Prefs — утилитный объект для хранения настроек в SharedPreferences
// Используется для: сохранения/чтения флага прохождения Onboarding, сохранения/чтения токена авторизации

object Prefs {
    private const val PREFS_NAME = "drive_next_prefs"
    private const val KEY_ONBOARDING_SHOWN = "onboarding_shown"
    private const val KEY_AUTH_TOKEN = "auth_token"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun isOnboardingShown(context: Context): Boolean {
        return getPrefs(context).getBoolean(KEY_ONBOARDING_SHOWN, false)
    }

    fun setOnboardingShown(context: Context, shown: Boolean = true) {
        getPrefs(context).edit().putBoolean(KEY_ONBOARDING_SHOWN, shown).apply()
    }

    fun getAuthToken(context: Context): String? {
        return getPrefs(context).getString(KEY_AUTH_TOKEN, null)
    }

    fun setAuthToken(context: Context, token: String) {
        getPrefs(context).edit().putString(KEY_AUTH_TOKEN, token).apply()
    }

    fun clearAuthToken(context: Context) {
        getPrefs(context).edit().remove(KEY_AUTH_TOKEN).apply()
    }
}