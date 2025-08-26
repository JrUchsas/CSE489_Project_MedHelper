package com.example.medhelper.data.api

import com.example.medhelper.data.model.LoginRequest
import com.example.medhelper.data.model.LoginResponse
import com.example.medhelper.data.model.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/register")
    suspend fun registerUser(@Body request: RegisterRequest): Response<LoginResponse>

    @POST("auth/login")
    suspend fun loginUser(@Body request: LoginRequest): Response<LoginResponse>
}
