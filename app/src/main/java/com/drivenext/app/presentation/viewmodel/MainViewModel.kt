package com.drivenext.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drivenext.app.data.repository.CarRepository
import com.drivenext.app.domain.model.Car
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel для главного экрана
 */
class MainViewModel(
    private val carRepository: CarRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        loadCars()
    }

    fun loadCars() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            carRepository.getAllCars()
                .onSuccess { cars ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        cars = cars,
                        error = null
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Не удалось загрузить данные"
                    )
                }
        }
    }

    fun searchCars(query: String) {
        viewModelScope.launch {
            if (query.isBlank()) {
                loadCars()
                return@launch
            }
            
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            carRepository.searchCars(query)
                .onSuccess { cars ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        cars = cars,
                        error = null
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Не удалось выполнить поиск"
                    )
                }
        }
    }
}

data class MainUiState(
    val isLoading: Boolean = false,
    val cars: List<Car> = emptyList(),
    val error: String? = null
)

