package com.drivenext.app.domain.model

/**
 * Модель бронирования
 */
data class Booking(
    val id: String,
    val carId: String,
    val userId: String,
    val startDate: String,
    val endDate: String,
    val totalPrice: Int,
    val insurancePrice: Int,
    val deposit: Int,
    val status: BookingStatus,
    val address: String? = null,
    val car: Car? = null
)

enum class BookingStatus {
    PENDING,
    APPROVED,
    ACTIVE,
    COMPLETED,
    CANCELLED
}

