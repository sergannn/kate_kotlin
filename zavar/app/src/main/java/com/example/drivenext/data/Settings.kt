package com.example.drivenext.data

data class Settings(
    // Уведомления
    val notificationsEnabled: Boolean = true,
    val rideNotifications: Boolean = true,
    val promotionNotifications: Boolean = true,
    val pushNotifications: Boolean = true,

    // Конфиденциальность
    val locationTracking: Boolean = true,
    val profileVisible: Boolean = true,
    val saveRideHistory: Boolean = true,

    // Предпочтения
    val language: String = "Русский",
    val currency: String = "RUB",
    val measurementUnit: String = "km",

    // Звук и вибрация
    val soundEnabled: Boolean = true,
    val vibrationEnabled: Boolean = true,
    val appSoundsEnabled: Boolean = true,

    // Тема
    val theme: String = "light", // light, dark, auto

    // Аккаунт
    val email: String = "",
    val phone: String = ""
)