package com.example.medhelper.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "family_members")
data class FamilyMember(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val relationship: String
)