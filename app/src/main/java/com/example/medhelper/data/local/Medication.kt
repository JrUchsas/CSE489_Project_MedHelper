package com.example.medhelper.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medications")
data class Medication(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val dosage: String,
    val schedule: String,
    val stockCount: Int,
    val familyMemberId: Int? = null // Null if for the main user
)