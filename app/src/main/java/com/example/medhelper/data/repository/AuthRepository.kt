package com.example.medhelper.data.repository

import com.example.medhelper.data.api.AuthApi
import com.example.medhelper.data.local.AuthToken
import com.example.medhelper.data.local.AuthTokenDao
import com.example.medhelper.data.model.LoginRequest
import com.example.medhelper.data.model.LoginResponse
import com.example.medhelper.data.model.RegisterRequest
import retrofit2.Response

class AuthRepository(private val authApi: AuthApi, private val authTokenDao: AuthTokenDao) {

    suspend fun register(request: RegisterRequest): Response<LoginResponse> {
        return authApi.registerUser(request)
    }

    suspend fun login(request: LoginRequest): Response<LoginResponse> {
        return authApi.loginUser(request)
    }

    suspend fun saveAuthToken(token: String) {
        authTokenDao.insertAuthToken(AuthToken(token = token))
    }

    suspend fun getAuthToken(): AuthToken? {
        return authTokenDao.getAuthToken()
    }

    suspend fun clearAuthToken() {
        authTokenDao.clearAuthToken()
    }
}
