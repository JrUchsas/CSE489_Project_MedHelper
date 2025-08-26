package com.example.medhelper.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medhelper.data.model.ErrorResponse
import com.example.medhelper.data.model.LoginRequest
import com.example.medhelper.data.model.RegisterRequest
import com.example.medhelper.data.repository.AuthRepository
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _loginState = MutableStateFlow<AuthResult>(AuthResult.Idle)
    val loginState: StateFlow<AuthResult> = _loginState

    private val _registerState = MutableStateFlow<AuthResult>(AuthResult.Idle)
    val registerState: StateFlow<AuthResult> = _registerState

    fun login(loginIdentifier: String, password: String) {
        _loginState.value = AuthResult.Loading
        viewModelScope.launch {
            try {
                val response = authRepository.login(LoginRequest(loginIdentifier, password))
                if (response.isSuccessful) {
                    response.body()?.let { loginResponse ->
                        authRepository.saveAuthToken(loginResponse.token)
                        _loginState.value = AuthResult.Success
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = try {
                        Gson().fromJson(errorBody, ErrorResponse::class.java).msg
                    } catch (e: Exception) {
                        "Login failed: ${response.code()}"
                    }
                    _loginState.value = AuthResult.Error(errorMessage)
                }
            } catch (e: Exception) {
                _loginState.value = AuthResult.Error(e.message ?: "An error occurred")
            }
        }
    }

    fun register(username: String, email: String, phoneNumber: String, password: String) {
        _registerState.value = AuthResult.Loading
        viewModelScope.launch {
            try {
                val response = authRepository.register(RegisterRequest(username, email, phoneNumber, password))
                if (response.isSuccessful) {
                    // Do not save token after registration, user needs to login
                    _registerState.value = AuthResult.Success
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMessage = try {
                        Gson().fromJson(errorBody, ErrorResponse::class.java).msg
                    } catch (e: Exception) {
                        "Registration failed: ${response.code()}"
                    }
                    _registerState.value = AuthResult.Error(errorMessage)
                }
            } catch (e: Exception) {
                _registerState.value = AuthResult.Error(e.message ?: "An error occurred")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.clearAuthToken()
            _loginState.value = AuthResult.Idle // Reset state after logout
            _registerState.value = AuthResult.Idle // Reset state after logout
        }
    }

    fun checkLoginStatus() {
        viewModelScope.launch {
            val token = authRepository.getAuthToken()
            if (token != null) {
                _loginState.value = AuthResult.Success
            } else {
                _loginState.value = AuthResult.Idle
            }
        }
    }
}

sealed class AuthResult {
    object Idle : AuthResult()
    object Loading : AuthResult()
    object Success : AuthResult()
    data class Error(val message: String) : AuthResult()
}
