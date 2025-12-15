package com.drivenext.app.utils

import android.content.Context
import android.content.SharedPreferences
import com.drivenext.app.domain.model.Booking
import com.drivenext.app.domain.model.BookingStatus
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Утилита для работы с SharedPreferences
 */
class Prefs(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        private const val PREFS_NAME = "drivenext_prefs"
        private const val KEY_IS_ONBOARDING_COMPLETED = "is_onboarding_completed"
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_BOOKINGS = "bookings"
        private const val KEY_CAR_DRAFT = "car_draft"
    }

    var isOnboardingCompleted: Boolean
        get() = prefs.getBoolean(KEY_IS_ONBOARDING_COMPLETED, false)
        set(value) = prefs.edit().putBoolean(KEY_IS_ONBOARDING_COMPLETED, value).apply()

    var accessToken: String?
        get() = prefs.getString(KEY_ACCESS_TOKEN, null)
        set(value) = prefs.edit().putString(KEY_ACCESS_TOKEN, value).apply()

    var userId: String?
        get() = prefs.getString(KEY_USER_ID, null)
        set(value) = prefs.edit().putString(KEY_USER_ID, value).apply()

    var userEmail: String?
        get() = prefs.getString(KEY_USER_EMAIL, null)
        set(value) = prefs.edit().putString(KEY_USER_EMAIL, value).apply()

    var isLoggedIn: Boolean
        get() = prefs.getBoolean(KEY_IS_LOGGED_IN, false)
        set(value) = prefs.edit().putBoolean(KEY_IS_LOGGED_IN, value).apply()

    fun saveBooking(booking: Booking) {
        val bookings = getBookings().toMutableList()
        bookings.add(booking)
        val json = gson.toJson(bookings)
        prefs.edit().putString(KEY_BOOKINGS, json).apply()
    }

    fun getBookings(): List<Booking> {
        val json = prefs.getString(KEY_BOOKINGS, null) ?: return emptyList()
        return try {
            val type = object : TypeToken<List<BookingData>>() {}.type
            val bookingDataList: List<BookingData> = gson.fromJson(json, type)
            bookingDataList.map { it.toBooking() }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun removeBooking(bookingId: String) {
        val bookings = getBookings().toMutableList()
        val updatedBookings = bookings.filter { it.id != bookingId }
        val json = gson.toJson(updatedBookings.map { BookingData.fromBooking(it) })
        prefs.edit().putString(KEY_BOOKINGS, json).apply()
    }

    fun saveCarDraft(carData: CarDraftData) {
        val json = gson.toJson(carData)
        prefs.edit().putString(KEY_CAR_DRAFT, json).apply()
    }

    fun getCarDraft(): CarDraftData? {
        val json = prefs.getString(KEY_CAR_DRAFT, null) ?: return null
        return gson.fromJson(json, CarDraftData::class.java)
    }

    fun clearCarDraft() {
        prefs.edit().remove(KEY_CAR_DRAFT).apply()
    }

    fun clear() {
        prefs.edit().clear().apply()
    }

    // Вспомогательный класс для сериализации/десериализации Booking
    private data class BookingData(
        val id: String,
        val carId: String,
        val userId: String,
        val startDate: String,
        val endDate: String,
        val totalPrice: Int,
        val insurancePrice: Int,
        val deposit: Int,
        val status: String
    ) {
        fun toBooking(): Booking {
            return Booking(
                id = id,
                carId = carId,
                userId = userId,
                startDate = startDate,
                endDate = endDate,
                totalPrice = totalPrice,
                insurancePrice = insurancePrice,
                deposit = deposit,
                status = BookingStatus.valueOf(status)
            )
        }

        companion object {
            fun fromBooking(booking: Booking): BookingData {
                return BookingData(
                    id = booking.id,
                    carId = booking.carId,
                    userId = booking.userId,
                    startDate = booking.startDate,
                    endDate = booking.endDate,
                    totalPrice = booking.totalPrice,
                    insurancePrice = booking.insurancePrice,
                    deposit = booking.deposit,
                    status = booking.status.name
                )
            }
        }
    }

    // Вспомогательный класс для временного хранения данных автомобиля при добавлении
    data class CarDraftData(
        val address: String? = null,
        val year: String? = null,
        val brand: String? = null,
        val model: String? = null,
        val transmission: String? = null,
        val mileage: String? = null,
        val description: String? = null,
        val photoUris: List<String> = emptyList()
    )
}

