package com.example.medhelper.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Medication::class, FamilyMember::class], version = 2, exportSchema = false)
abstract class MedHelperDatabase : RoomDatabase() {
    abstract fun medicationDao(): MedicationDao
    abstract fun familyMemberDao(): FamilyMemberDao
}