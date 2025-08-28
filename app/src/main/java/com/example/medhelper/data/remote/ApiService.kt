package com.example.medhelper.data.remote

import com.example.medhelper.data.local.FamilyMember
import com.example.medhelper.data.local.Medication
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.DELETE
import retrofit2.http.Path

interface ApiService {

    @POST("api/auth/register")
    suspend fun registerUser(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("api/auth/login")
    suspend fun loginUser(@Body request: LoginRequest): Response<AuthResponse>

    @POST("api/medications")
    suspend fun addMedication(@Header("x-auth-token") token: String, @Body medication: Medication): Response<Medication>

    @GET("api/medications")
    suspend fun getMedications(@Header("x-auth-token") token: String): Response<List<Medication>>

    @DELETE("api/medications/{id}")
    suspend fun deleteMedication(@Header("x-auth-token") token: String, @Path("id") id: String): Response<Unit>

    @POST("api/family")
    suspend fun addFamilyMember(@Header("x-auth-token") token: String, @Body familyMember: FamilyMember): Response<FamilyMember>

    @GET("api/family")
    suspend fun getFamilyMembers(@Header("x-auth-token") token: String): Response<List<FamilyMember>>

    @DELETE("api/family/{id}")
    suspend fun deleteFamilyMember(@Header("x-auth-token") token: String, @Path("id") id: String): Response<Unit>
}