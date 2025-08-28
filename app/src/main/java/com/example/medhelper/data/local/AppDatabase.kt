package com.example.medhelper.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [AuthToken::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun authTokenDao(): AuthTokenDao
}
