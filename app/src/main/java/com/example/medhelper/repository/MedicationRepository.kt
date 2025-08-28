package com.example.medhelper.repository

import com.example.medhelper.data.local.Medication
import com.example.medhelper.data.local.MedicationDao
import com.example.medhelper.data.remote.ApiService
import com.example.medhelper.data.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MedicationRepository @Inject constructor(
    private val medicationDao: MedicationDao,
    private val apiService: ApiService,
    private val userPreferencesRepository: UserPreferencesRepository
) {

    fun getAllMedications(): Flow<List<Medication>> = medicationDao.getAllMedications()

    suspend fun addMedication(token: String, medication: Medication) {
        apiService.addMedication(token, medication)
        medicationDao.insertMedication(medication) // Save to local DB as well
    }

    suspend fun decrementMedicationStock(medicationId: Int) {
        medicationDao.decrementStockCount(medicationId)
        // TODO: Implement API call to update stock on backend
    }

    suspend fun deleteMedication(medication: Medication) {
        medicationDao.deleteMedication(medication)
        medication.id?.let {
            apiService.deleteMedication(userPreferencesRepository.authToken.first()!!, it.toString())
        }
    }

    suspend fun fetchAndCacheMedications(token: String) {
        val response = apiService.getMedications(token)
        if (response.isSuccessful && response.body() != null) {
            medicationDao.clearAllMedications() // Clear existing data
            response.body()!!.forEach { medication ->
                medicationDao.insertMedication(medication)
            }
        }
    }
}