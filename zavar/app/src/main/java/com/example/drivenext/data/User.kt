package com.example.drivenext.data

data class User(
    val id: String = "1",
    val name: String = "Иван Иванов",
    val email: String = "user@example.com",
    val avatarUrl: String? = null,
    val phone: String = "+7 (999) 123-45-67"
)