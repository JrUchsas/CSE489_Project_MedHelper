package com.example.medhelper.di

import android.content.Context
import androidx.room.Room
import com.example.medhelper.data.api.AuthApi
import com.example.medhelper.data.local.AppDatabase
import com.example.medhelper.data.local.AuthTokenDao
import com.example.medhelper.data.repository.AuthRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {

    private const val BASE_URL = "http://192.168.0.116:5000/api/"

    fun provideAuthInterceptor(authTokenDao: AuthTokenDao): Interceptor {
        return Interceptor {
            val requestBuilder = it.request().newBuilder()
            val token = runBlocking { authTokenDao.getAuthToken()?.token }
            token?.let {
                requestBuilder.addHeader("x-auth-token", it)
            }
            it.proceed(requestBuilder.build())
        }
    }

    fun provideOkHttpClient(authInterceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun provideAuthApi(retrofit: Retrofit): AuthApi {
        return retrofit.create(AuthApi::class.java)
    }

    fun provideAppDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "medhelper_db"
        ).build()
    }

    fun provideAuthTokenDao(appDatabase: AppDatabase): AuthTokenDao {
        return appDatabase.authTokenDao()
    }

    fun provideAuthRepository(authApi: AuthApi, authTokenDao: AuthTokenDao): AuthRepository {
        return AuthRepository(authApi, authTokenDao)
    }
}
