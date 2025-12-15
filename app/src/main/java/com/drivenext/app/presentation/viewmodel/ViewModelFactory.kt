package com.drivenext.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.drivenext.app.data.repository.AuthRepository
import com.drivenext.app.data.repository.CarRepository

/**
 * Фабрика для создания ViewModels
 */
class ViewModelFactory(
    private val authRepository: AuthRepository,
    private val carRepository: CarRepository? = null
) : ViewModelProvider.Factory {
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(authRepository) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                requireNotNull(carRepository) { "CarRepository is required for MainViewModel" }
                MainViewModel(carRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}

