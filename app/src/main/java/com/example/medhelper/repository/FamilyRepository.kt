package com.example.medhelper.repository

import com.example.medhelper.data.local.FamilyMember
import com.example.medhelper.data.local.FamilyMemberDao
import com.example.medhelper.data.remote.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import com.example.medhelper.data.UserPreferencesRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FamilyRepository @Inject constructor(
    private val familyMemberDao: FamilyMemberDao,
    private val apiService: ApiService,
    private val userPreferencesRepository: UserPreferencesRepository
) {

    fun getAllFamilyMembers(): Flow<List<FamilyMember>> = familyMemberDao.getAllFamilyMembers()

    suspend fun addFamilyMember(token: String, familyMember: FamilyMember) {
        apiService.addFamilyMember(token, familyMember)
        familyMemberDao.insertFamilyMember(familyMember) // Save to local DB as well
    }

    suspend fun fetchAndCacheFamilyMembers(token: String) {
        val response = apiService.getFamilyMembers(token)
        if (response.isSuccessful && response.body() != null) {
            familyMemberDao.clearAllFamilyMembers() // Clear existing data
            response.body()!!.forEach { familyMember ->
                familyMemberDao.insertFamilyMember(familyMember)
            }
        }
    }

    suspend fun deleteFamilyMember(familyMember: FamilyMember) {
        familyMemberDao.deleteFamilyMember(familyMember)
        familyMember.id?.let {
            apiService.deleteFamilyMember(userPreferencesRepository.authToken.first()!!, it.toString())
        }
    }
}