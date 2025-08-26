package com.example.medhelper.data.model

data class RegisterRequest(
    val username: String,
    val email: String,
    val phoneNumber: String,
    val password: String
)
