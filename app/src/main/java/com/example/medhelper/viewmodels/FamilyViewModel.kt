package com.example.medhelper.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medhelper.data.UserPreferencesRepository
import com.example.medhelper.data.local.FamilyMember
import com.example.medhelper.repository.FamilyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FamilyViewModel @Inject constructor(
    private val familyRepository: FamilyRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _familyMembers = MutableStateFlow<List<FamilyMember>>(emptyList())
    val familyMembers: StateFlow<List<FamilyMember>> = _familyMembers

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        viewModelScope.launch {
            familyRepository.getAllFamilyMembers().collect { members ->
                _familyMembers.value = members
            }
        }
    }

    fun addFamilyMember(familyMember: FamilyMember) {
        viewModelScope.launch {
            try {
                val token = userPreferencesRepository.authToken.first()
                token?.let {
                    familyRepository.addFamilyMember(it, familyMember)
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

    fun fetchFamilyMembers() {
        viewModelScope.launch {
            try {
                val token = userPreferencesRepository.authToken.first()
                token?.let {
                    familyRepository.fetchAndCacheFamilyMembers(it)
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

    fun deleteFamilyMember(familyMember: FamilyMember) {
        viewModelScope.launch {
            try {
                familyRepository.deleteFamilyMember(familyMember)
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