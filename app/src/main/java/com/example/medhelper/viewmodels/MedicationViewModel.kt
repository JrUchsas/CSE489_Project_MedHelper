package com.example.medhelper.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medhelper.data.UserPreferencesRepository
import com.example.medhelper.data.local.Medication
import com.example.medhelper.repository.MedicationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicationViewModel @Inject constructor(
    private val medicationRepository: MedicationRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _medications = MutableStateFlow<List<Medication>>(emptyList())
    val medications: StateFlow<List<Medication>> = _medications

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        viewModelScope.launch {
            medicationRepository.getAllMedications().collect { meds ->
                _medications.value = meds
            }
        }
    }

    fun addMedication(medication: Medication) {
        viewModelScope.launch {
            try {
                val token = userPreferencesRepository.authToken.first() // Get current token
                token?.let {
                    medicationRepository.addMedication(it, medication)
                }
            } catch (e: Exception) {
                _error.value = when (e) {
                    is java.net.UnknownHostException -> "No internet connection. Please check your network settings."
                    is java.net.ConnectException -> "Unable to connect to the server. Please try again later."
                    else -> e.message ?: "An unexpected network error occurred."
                }
            }
        }
    }

    fun decrementMedicationStock(medicationId: Int) {
        viewModelScope.launch {
            try {
                medicationRepository.decrementMedicationStock(medicationId)
            } catch (e: Exception) {
                _error.value = when (e) {
                    is java.net.UnknownHostException -> "No internet connection. Please check your network settings."
                    is java.net.ConnectException -> "Unable to connect to the server. Please try again later."
                    else -> e.message ?: "An unexpected network error occurred."
                }
            }
        }
    }

    fun deleteMedication(medication: Medication) {
        viewModelScope.launch {
            try {
                medicationRepository.deleteMedication(medication)
            } catch (e: Exception) {
                _error.value = when (e) {
                    is java.net.UnknownHostException -> "No internet connection. Please check your network settings."
                    is java.net.ConnectException -> "Unable to connect to the server. Please try again later."
                    else -> e.message ?: "An unexpected network error occurred."
                }
            }
        }
    }

    fun fetchMedications() {
        viewModelScope.launch {
            try {
                val token = userPreferencesRepository.authToken.first()
                token?.let {
                    medicationRepository.fetchAndCacheMedications(it)
                }
            } catch (e: Exception) {
                _error.value = when (e) {
                    is java.net.UnknownHostException -> "No internet connection. Please check your network settings."
                    is java.net.ConnectException -> "Unable to connect to the server. Please try again later."
                    else -> e.message ?: "An unexpected network error occurred."
                }
            }
        }
    }
}