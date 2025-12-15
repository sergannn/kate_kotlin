package com.drivenext.app.data.repository

import android.content.Context
import com.drivenext.app.domain.model.Booking
import com.drivenext.app.domain.model.BookingStatus
import com.drivenext.app.utils.Prefs
import kotlinx.coroutines.delay

/**
 * Реализация репозитория бронирований
 * Использует SharedPreferences для временного хранения
 * TODO: Интегрировать с Supabase
 */
class BookingRepositoryImpl(private val context: Context? = null) : BookingRepository {

    private val prefs: Prefs? = context?.let { Prefs(it) }

    override suspend fun createBooking(
        carId: String,
        startDate: String,
        endDate: String,
        totalPrice: Int,
        insurancePrice: Int,
        deposit: Int
    ): Result<Booking> {
        delay(1000)
        val booking = Booking(
            id = "booking_${System.currentTimeMillis()}",
            carId = carId,
            userId = "user_1",
            startDate = startDate,
            endDate = endDate,
            totalPrice = totalPrice,
            insurancePrice = insurancePrice,
            deposit = deposit,
            status = BookingStatus.PENDING
        )
        
        // Сохраняем бронирование в SharedPreferences
        prefs?.saveBooking(booking)
        
        return Result.success(booking)
    }

    override suspend fun getUserBookings(): Result<List<Booking>> {
        delay(500)
        val bookings = prefs?.getBookings() ?: emptyList()
        return Result.success(bookings)
    }

    override suspend fun getBookingById(id: String): Result<Booking> {
        delay(500)
        val bookings = prefs?.getBookings() ?: emptyList()
        val booking = bookings.find { it.id == id }
        return if (booking != null) {
            Result.success(booking)
        } else {
            Result.failure(Exception("Бронирование не найдено"))
        }
    }

    override suspend fun cancelBooking(id: String): Result<Unit> {
        delay(500)
        prefs?.removeBooking(id)
        return Result.success(Unit)
    }
}

