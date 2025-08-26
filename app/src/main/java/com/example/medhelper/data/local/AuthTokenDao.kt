package com.example.medhelper.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AuthTokenDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuthToken(authToken: AuthToken)

    @Query("SELECT * FROM auth_tokens LIMIT 1")
    suspend fun getAuthToken(): AuthToken?

    @Query("DELETE FROM auth_tokens")
    suspend fun clearAuthToken()
}
