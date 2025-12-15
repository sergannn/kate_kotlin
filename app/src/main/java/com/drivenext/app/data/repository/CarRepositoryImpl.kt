package com.drivenext.app.data.repository

import com.drivenext.app.domain.model.Car
import kotlinx.coroutines.delay

/**
 * Реализация репозитория автомобилей
 * TODO: Интегрировать с Supabase
 */
class CarRepositoryImpl : CarRepository {

    // Временные мок-данные
    private val mockCars = listOf(
        Car(
            id = "1",
            brand = "Toyota",
            model = "Camry",
            year = 2022,
            pricePerDay = 3000,
            fuelType = "Бензин",
            transmission = "Автомат",
            seats = 5,
            imageUrl = "",
            description = "Комфортный седан для городских поездок",
            address = "Москва, ул. Ленина, 1",
            available = true,
            mileage = 15000
        ),
        Car(
            id = "2",
            brand = "BMW",
            model = "X5",
            year = 2023,
            pricePerDay = 8000,
            fuelType = "Бензин",
            transmission = "Автомат",
            seats = 7,
            imageUrl = "",
            description = "Премиальный внедорожник",
            address = "Москва, ул. Пушкина, 10",
            available = true,
            mileage = 5000
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
        // TODO: Реализовать через Supabase
        return Result.success(emptyList())
    }

    override suspend fun addToFavorites(carId: String): Result<Unit> {
        delay(300)
        // TODO: Реализовать через Supabase
        return Result.success(Unit)
    }

    override suspend fun removeFromFavorites(carId: String): Result<Unit> {
        delay(300)
        // TODO: Реализовать через Supabase
        return Result.success(Unit)
    }

    override suspend fun isFavorite(carId: String): Boolean {
        // TODO: Реализовать через Supabase
        return false
    }
}

