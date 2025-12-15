package com.drivenext.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drivenext.app.data.repository.AuthRepository
import com.drivenext.app.utils.Validator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel для экрана входа
 */
class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun validateEmail(email: String) {
        val error = Validator.getEmailError(email)
        _uiState.value = _uiState.value.copy(emailError = error)
    }

    fun validatePassword(password: String) {
        val error = Validator.getPasswordError(password)
        _uiState.value = _uiState.value.copy(passwordError = error)
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            val emailError = Validator.getEmailError(email)
            val passwordError = Validator.getPasswordError(password)
            
            if (emailError != null || passwordError != null) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    emailError = emailError,
                    passwordError = passwordError
                )
                return@launch
            }

            authRepository.login(email, password)
                .onSuccess { token ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSuccess = true
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Ошибка входа"
                    )
                }
        }
    }

    fun loginWithGoogle(idToken: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            authRepository.loginWithGoogle(idToken)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isSuccess = true
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Ошибка входа через Google"
                    )
                }
        }
    }
}

data class LoginUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null,
    val error: String? = null
)

