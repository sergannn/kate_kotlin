package com.drivenext.app.data.repository

import android.content.Context
import com.drivenext.app.domain.model.Car
import com.drivenext.app.utils.Prefs
import kotlinx.coroutines.delay

/**
 * Реализация репозитория автомобилей
 * TODO: Интегрировать с Supabase
 */
class CarRepositoryImpl(private val context: Context? = null) : CarRepository {
    
    private val prefs: Prefs? = context?.let { Prefs(it) }

    // Временные мок-данные
    private val mockCars = listOf(
        Car(
            id = "1",
            brand = "Mercedes-Benz",
            model = "S 500 Sedan",
            year = 2023,
            pricePerDay = 2500,
            fuelType = "Бензин",
            transmission = "A/T",
            seats = 5,
            imageUrl = "",
            description = "Роскошный седан премиум-класса с передовыми технологиями",
            address = "Москва, ул. Тверская, 10",
            available = true,
            mileage = 12000
        ),
        Car(
            id = "2",
            brand = "Mercedes-Benz",
            model = "GLE 350",
            year = 2022,
            pricePerDay = 9000,
            fuelType = "Бензин",
            transmission = "A/T",
            seats = 5,
            imageUrl = "",
            description = "Премиальный внедорожник с полным приводом 4WD",
            address = "Москва, ул. Ленинградский проспект, 15",
            available = true,
            mileage = 8000
        ),
        Car(
            id = "3",
            brand = "Mercedes-Benz",
            model = "S 500 Sedan",
            year = 2024,
            pricePerDay = 2500,
            fuelType = "Бензин",
            transmission = "A/T",
            seats = 5,
            imageUrl = "",
            description = "Новейший седан с улучшенной системой безопасности",
            address = "Москва, ул. Арбат, 25",
            available = true,
            mileage = 5000
        ),
        Car(
            id = "4",
            brand = "Mercedes-Benz",
            model = "GLE 350",
            year = 2023,
            pricePerDay = 9000,
            fuelType = "Бензин",
            transmission = "A/T",
            seats = 5,
            imageUrl = "",
            description = "Современный внедорожник с комфортным салоном",
            address = "Москва, ул. Кутузовский проспект, 20",
            available = true,
            mileage = 10000
        ),
        Car(
            id = "5",
            brand = "Mercedes-Benz",
            model = "S 500 Sedan",
            year = 2022,
            pricePerDay = 2500,
            fuelType = "Бензин",
            transmission = "A/T",
            seats = 5,
            imageUrl = "",
            description = "Элегантный седан для деловых поездок",
            address = "Москва, ул. Новый Арбат, 30",
            available = true,
            mileage = 18000
        )
    )

    override suspend fun getAllCars(): Result<List<Car>> {
        delay(1000) // Имитация запроса
        return Result.success(mockCars)
    }

    override suspend fun getCarById(id: String): Result<Car> {
        delay(500)
        val car = mockCars.find { it.id == id }
        return if (car != null) {
            Result.success(car)
        } else {
            Result.failure(Exception("Автомобиль не найден"))
        }
    }

    override suspend fun searchCars(query: String): Result<List<Car>> {
        delay(500)
        val filtered = mockCars.filter {
            it.brand.contains(query, ignoreCase = true) ||
            it.model.contains(query, ignoreCase = true)
        }
        return Result.success(filtered)
    }

    override suspend fun getFavoriteCars(): Result<List<Car>> {
        delay(500)
        return if (prefs != null) {
            val favoriteIds = prefs.getFavoriteCarIds()
            val favoriteCars = mockCars.filter { it.id in favoriteIds }
            Result.success(favoriteCars)
        } else {
            Result.success(emptyList())
        }
    }

    override suspend fun addToFavorites(carId: String): Result<Unit> {
        delay(300)
        return if (prefs != null) {
            prefs.addFavoriteCar(carId)
            Result.success(Unit)
        } else {
            Result.failure(Exception("Context не предоставлен"))
        }
    }

    override suspend fun removeFromFavorites(carId: String): Result<Unit> {
        delay(300)
        return if (prefs != null) {
            prefs.removeFavoriteCar(carId)
            Result.success(Unit)
        } else {
            Result.failure(Exception("Context не предоставлен"))
        }
    }

    override suspend fun isFavorite(carId: String): Boolean {
        return prefs?.isFavoriteCar(carId) ?: false
    }
}

