package com.example.medhelper.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medhelper.data.UserPreferencesRepository
import com.example.medhelper.data.remote.ApiService
import com.example.medhelper.data.remote.LoginRequest
import com.example.medhelper.data.remote.RegisterRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val apiService: ApiService,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _authToken = MutableStateFlow<String?>(null)
    val authToken: StateFlow<String?> = _authToken

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _registrationSuccess = MutableStateFlow(false)
    val registrationSuccess: StateFlow<Boolean> = _registrationSuccess

    init {
        viewModelScope.launch {
            userPreferencesRepository.authToken.collect { token ->
                _authToken.value = token
            }
        }
    }

    fun login(loginIdentifier: String, password: String) {
        _isLoading.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                val response = apiService.loginUser(LoginRequest(loginIdentifier, password))
                if (response.isSuccessful && response.body() != null) {
                    userPreferencesRepository.saveAuthToken(response.body()!!.token)
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorResponse = try {
                        com.google.gson.Gson().fromJson(errorBody, com.example.medhelper.data.remote.ErrorResponse::class.java)
                    } catch (e: Exception) {
                        null
                    }
                    _error.value = errorResponse?.msg ?: errorBody ?: "Login failed"
                }
            } catch (e: Exception) {
                _error.value = when (e) {
                    is java.net.UnknownHostException -> "No internet connection. Please check your network settings."
                    is java.net.ConnectException -> "Unable to connect to the server. Please try again later."
                    else -> e.message ?: "An unexpected network error occurred."
                }
            }
            _isLoading.value = false
        }
    }

    fun register(username: String, email: String, phoneNumber: String, password: String) {
        _isLoading.value = true
        _error.value = null
        _registrationSuccess.value = false
        viewModelScope.launch {
            try {
                val response = apiService.registerUser(RegisterRequest(username, email, phoneNumber, password))
                if (response.isSuccessful && response.body() != null) {
                    // Do not save token after registration, navigate to login instead
                    _error.value = null // Clear any previous errors
                    _registrationSuccess.value = true
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorResponse = try {
                        com.google.gson.Gson().fromJson(errorBody, com.example.medhelper.data.remote.ErrorResponse::class.java)
                    } catch (e: Exception) {
                        null
                    }
                    _error.value = errorResponse?.msg ?: errorBody ?: "Registration failed"
                }
            } catch (e: Exception) {
                _error.value = when (e) {
                    is java.net.UnknownHostException -> "No internet connection. Please check your network settings."
                    is java.net.ConnectException -> "Unable to connect to the server. Please try again later."
                    else -> e.message ?: "An unexpected network error occurred."
                }
            }
            _isLoading.value = false
        }
    }

    fun logout() {
        viewModelScope.launch {
            userPreferencesRepository.clearAuthToken()
        }
    }
}