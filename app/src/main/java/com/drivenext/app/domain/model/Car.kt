package com.drivenext.app.domain.model

/**
 * Модель автомобиля
 */
data class Car(
    val id: String,
    val brand: String,
    val model: String,
    val year: Int,
    val pricePerDay: Int,
    val fuelType: String,
    val transmission: String,
    val seats: Int,
    val imageUrl: String,
    val description: String? = null,
    val address: String? = null,
    val available: Boolean = true,
    val mileage: Int? = null
)

