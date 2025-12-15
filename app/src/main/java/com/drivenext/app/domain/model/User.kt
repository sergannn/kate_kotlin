package com.drivenext.app.domain.model

/**
 * Модель пользователя
 */
data class User(
    val id: String,
    val email: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val middleName: String? = null,
    val avatarUrl: String? = null,
    val phone: String? = null,
    val dateOfBirth: String? = null,
    val gender: String? = null,
    val driverLicenseNumber: String? = null,
    val driverLicenseIssueDate: String? = null
) {
    val fullName: String
        get() = listOfNotNull(lastName, firstName, middleName).joinToString(" ")
}

