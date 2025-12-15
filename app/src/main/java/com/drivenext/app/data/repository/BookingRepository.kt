package com.drivenext.app.data.repository

import com.drivenext.app.domain.model.Booking

/**
 * Репозиторий для работы с бронированиями
 */
interface BookingRepository {
    suspend fun createBooking(
        carId: String,
        startDate: String,
        endDate: String,
        totalPrice: Int,
        insurancePrice: Int,
        deposit: Int
    ): Result<Booking>
    suspend fun getUserBookings(): Result<List<Booking>>
    suspend fun getBookingById(id: String): Result<Booking>
    suspend fun cancelBooking(id: String): Result<Unit>
}

