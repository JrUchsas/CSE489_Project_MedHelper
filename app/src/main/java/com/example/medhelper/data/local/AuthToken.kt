package com.example.medhelper.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "auth_tokens")
data class AuthToken(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val token: String
)
