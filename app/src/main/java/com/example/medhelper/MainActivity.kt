package com.example.medhelper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.medhelper.data.local.AppDatabase
import com.example.medhelper.data.repository.AuthRepository
import com.example.medhelper.di.NetworkModule
import com.example.medhelper.ui.auth.LoginScreen
import com.example.medhelper.ui.auth.RegisterScreen
import com.example.medhelper.ui.dashboard.DashboardScreen
import com.example.medhelper.ui.theme.MedHelperTheme
import com.example.medhelper.viewmodel.auth.AuthViewModel
import com.example.medhelper.viewmodel.auth.AuthViewModelFactory

class MainActivity : ComponentActivity() {
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val appDatabase = NetworkModule.provideAppDatabase(applicationContext)
        val authTokenDao = NetworkModule.provideAuthTokenDao(appDatabase)
        val authInterceptor = NetworkModule.provideAuthInterceptor(authTokenDao)
        val okHttpClient = NetworkModule.provideOkHttpClient(authInterceptor)
        val retrofit = NetworkModule.provideRetrofit(okHttpClient)
        val authApi = NetworkModule.provideAuthApi(retrofit)
        val authRepository = NetworkModule.provideAuthRepository(authApi, authTokenDao)
        val authViewModelFactory = AuthViewModelFactory(authRepository)
        authViewModel = authViewModelFactory.create(AuthViewModel::class.java)

        setContent {
            MedHelperTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MedHelperApp(authViewModel = authViewModel)
                }
            }
        }
    }
}

@Composable
fun MedHelperApp(authViewModel: AuthViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController = navController, authViewModel = authViewModel)
        }
        composable("register") {
            RegisterScreen(navController = navController, authViewModel = authViewModel)
        }
        composable("dashboard") {
            DashboardScreen(navController = navController, authViewModel = authViewModel)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MedHelperTheme {
        // Preview content if needed
    }
}
