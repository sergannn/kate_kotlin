package com.drivenext.app.data.repository

import com.drivenext.app.domain.model.Car

/**
 * Репозиторий для работы с автомобилями
 */
interface CarRepository {
    suspend fun getAllCars(): Result<List<Car>>
    suspend fun getCarById(id: String): Result<Car>
    suspend fun searchCars(query: String): Result<List<Car>>
    suspend fun getFavoriteCars(): Result<List<Car>>
    suspend fun addToFavorites(carId: String): Result<Unit>
    suspend fun removeFromFavorites(carId: String): Result<Unit>
    suspend fun isFavorite(carId: String): Boolean
}

