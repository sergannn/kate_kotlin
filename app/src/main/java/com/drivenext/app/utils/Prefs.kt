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
        private const val KEY_PROFILE_PHOTO_URI = "profile_photo_uri"
        private const val KEY_LICENSE_PHOTO_URI = "license_photo_uri"
        private const val KEY_PASSPORT_PHOTO_URI = "passport_photo_uri"
        // Данные регистрации
        private const val KEY_REG_EMAIL = "reg_email"
        private const val KEY_REG_PASSWORD = "reg_password"
        private const val KEY_REG_FIRST_NAME = "reg_first_name"
        private const val KEY_REG_LAST_NAME = "reg_last_name"
        private const val KEY_REG_MIDDLE_NAME = "reg_middle_name"
        private const val KEY_REG_BIRTH_DATE = "reg_birth_date"
        private const val KEY_REG_GENDER = "reg_gender"
        private const val KEY_REG_LICENSE_NUMBER = "reg_license_number"
        private const val KEY_REG_LICENSE_ISSUE_DATE = "reg_license_issue_date"
        // Избранные машины
        private const val KEY_FAVORITE_CARS = "favorite_cars"
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

    // Сохранение URI фотографий регистрации
    var profilePhotoUri: String?
        get() = prefs.getString(KEY_PROFILE_PHOTO_URI, null)
        set(value) = prefs.edit().putString(KEY_PROFILE_PHOTO_URI, value).apply()

    var licensePhotoUri: String?
        get() = prefs.getString(KEY_LICENSE_PHOTO_URI, null)
        set(value) = prefs.edit().putString(KEY_LICENSE_PHOTO_URI, value).apply()

    var passportPhotoUri: String?
        get() = prefs.getString(KEY_PASSPORT_PHOTO_URI, null)
        set(value) = prefs.edit().putString(KEY_PASSPORT_PHOTO_URI, value).apply()

    fun clearRegistrationPhotos() {
        prefs.edit()
            .remove(KEY_PROFILE_PHOTO_URI)
            .remove(KEY_LICENSE_PHOTO_URI)
            .remove(KEY_PASSPORT_PHOTO_URI)
            .apply()
    }

    // Данные регистрации
    var registrationEmail: String?
        get() = prefs.getString(KEY_REG_EMAIL, null)
        set(value) = prefs.edit().putString(KEY_REG_EMAIL, value).apply()

    var registrationPassword: String?
        get() = prefs.getString(KEY_REG_PASSWORD, null)
        set(value) = prefs.edit().putString(KEY_REG_PASSWORD, value).apply()

    var registrationFirstName: String?
        get() = prefs.getString(KEY_REG_FIRST_NAME, null)
        set(value) = prefs.edit().putString(KEY_REG_FIRST_NAME, value).apply()

    var registrationLastName: String?
        get() = prefs.getString(KEY_REG_LAST_NAME, null)
        set(value) = prefs.edit().putString(KEY_REG_LAST_NAME, value).apply()

    var registrationMiddleName: String?
        get() = prefs.getString(KEY_REG_MIDDLE_NAME, null)
        set(value) = prefs.edit().putString(KEY_REG_MIDDLE_NAME, value).apply()

    var registrationBirthDate: String?
        get() = prefs.getString(KEY_REG_BIRTH_DATE, null)
        set(value) = prefs.edit().putString(KEY_REG_BIRTH_DATE, value).apply()

    var registrationGender: String?
        get() = prefs.getString(KEY_REG_GENDER, null)
        set(value) = prefs.edit().putString(KEY_REG_GENDER, value).apply()

    var registrationLicenseNumber: String?
        get() = prefs.getString(KEY_REG_LICENSE_NUMBER, null)
        set(value) = prefs.edit().putString(KEY_REG_LICENSE_NUMBER, value).apply()

    var registrationLicenseIssueDate: String?
        get() = prefs.getString(KEY_REG_LICENSE_ISSUE_DATE, null)
        set(value) = prefs.edit().putString(KEY_REG_LICENSE_ISSUE_DATE, value).apply()

    fun clearRegistrationData() {
        prefs.edit()
            .remove(KEY_REG_EMAIL)
            .remove(KEY_REG_PASSWORD)
            .remove(KEY_REG_FIRST_NAME)
            .remove(KEY_REG_LAST_NAME)
            .remove(KEY_REG_MIDDLE_NAME)
            .remove(KEY_REG_BIRTH_DATE)
            .remove(KEY_REG_GENDER)
            .remove(KEY_REG_LICENSE_NUMBER)
            .remove(KEY_REG_LICENSE_ISSUE_DATE)
            .remove(KEY_PROFILE_PHOTO_URI)
            .remove(KEY_LICENSE_PHOTO_URI)
            .remove(KEY_PASSPORT_PHOTO_URI)
            .apply()
    }

    // Избранные машины
    fun getFavoriteCarIds(): Set<String> {
        val json = prefs.getString(KEY_FAVORITE_CARS, null) ?: return emptySet()
        return try {
            val type = object : TypeToken<Set<String>>() {}.type
            gson.fromJson(json, type) ?: emptySet()
        } catch (e: Exception) {
            emptySet()
        }
    }

    fun addFavoriteCar(carId: String) {
        val favorites = getFavoriteCarIds().toMutableSet()
        favorites.add(carId)
        val json = gson.toJson(favorites)
        prefs.edit().putString(KEY_FAVORITE_CARS, json).apply()
    }

    fun removeFavoriteCar(carId: String) {
        val favorites = getFavoriteCarIds().toMutableSet()
        favorites.remove(carId)
        val json = gson.toJson(favorites)
        prefs.edit().putString(KEY_FAVORITE_CARS, json).apply()
    }

    fun isFavoriteCar(carId: String): Boolean {
        return getFavoriteCarIds().contains(carId)
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

