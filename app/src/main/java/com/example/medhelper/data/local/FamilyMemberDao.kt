package com.example.medhelper.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FamilyMemberDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFamilyMember(familyMember: FamilyMember)

    @Query("SELECT * FROM family_members")
    fun getAllFamilyMembers(): Flow<List<FamilyMember>>

    @Query("DELETE FROM family_members")
    suspend fun clearAllFamilyMembers()

    @Delete
    suspend fun deleteFamilyMember(familyMember: FamilyMember)
}