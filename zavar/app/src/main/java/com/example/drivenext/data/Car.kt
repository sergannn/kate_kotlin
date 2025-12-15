package com.example.drivenext.data

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
    val available: Boolean = true
)