package com.example.medhelper.data.remote

data class LoginRequest(
    val loginIdentifier: String,
    val password: String
)

data class RegisterRequest(
    val username: String,
    val email: String,
    val phoneNumber: String,
    val password: String
)

data class AuthResponse(
    val token: String
)

data class ErrorResponse(
    val msg: String
)