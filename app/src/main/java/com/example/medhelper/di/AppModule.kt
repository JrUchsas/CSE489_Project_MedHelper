package com.example.medhelper.di

import android.content.Context
import androidx.room.Room
import com.example.medhelper.data.UserPreferencesRepository
import com.example.medhelper.data.local.MedHelperDatabase
import com.example.medhelper.data.remote.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMedHelperDatabase(@ApplicationContext context: Context): MedHelperDatabase {
        return Room.databaseBuilder(
            context,
            MedHelperDatabase::class.java,
            "medhelper_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideMedicationDao(database: MedHelperDatabase) = database.medicationDao()

    @Provides
    @Singleton
    fun provideFamilyMemberDao(database: MedHelperDatabase) = database.familyMemberDao()

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl("http://192.168.0.116:5000/") // Use your local IP for physical device
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserPreferencesRepository(@ApplicationContext context: Context): UserPreferencesRepository {
        return UserPreferencesRepository(context)
    }
}