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
import com.example.medhelper.ui.screens.AddMedicationScreen
import com.example.medhelper.ui.screens.DashboardScreen
import com.example.medhelper.ui.screens.FamilyScreen
import com.example.medhelper.ui.screens.LoginScreen
import com.example.medhelper.ui.screens.RegisterScreen
import com.example.medhelper.ui.screens.LowStockReminderScreen // Import the new screen
import com.example.medhelper.ui.theme.MedHelperTheme // Import MedHelperTheme
import dagger.hilt.android.AndroidEntryPoint

import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.material3.Divider
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

import androidx.compose.ui.unit.dp // Added import for dp
import androidx.compose.material3.HorizontalDivider // Import HorizontalDivider
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.material3.Icon

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MedHelperAppContent() // Call a Composable function here
        }
    }
}

@Composable
fun MedHelperAppContent() {
    MedHelperTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            MedHelperApp()
        }
    }
}

@Composable
fun MedHelperApp() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = navBackStackEntry?.destination?.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                                Text("MedHelper Menu", modifier = Modifier.padding(16.dp))
                HorizontalDivider()
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Dashboard") },
                    label = { Text("Dashboard") },
                    selected = currentRoute == "dashboard",
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("dashboard") {
                            popUpTo("dashboard") { inclusive = true }
                        }
                    }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Family Profiles") },
                    label = { Text("Family Profiles") },
                    selected = currentRoute == "family",
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("family")
                    }
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Notifications, contentDescription = "Low Stock Reminders") },
                    label = { Text("Low Stock Reminders") },
                    selected = currentRoute == "low_stock_reminder",
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("low_stock_reminder")
                    }
                )
            }
        }
    ) {
        NavHost(navController = navController, startDestination = "login") {
            composable("login") {
                LoginScreen(navController = navController)
            }
            composable("register") {
                RegisterScreen(navController = navController)
            }
            composable("dashboard") {
                DashboardScreen(navController = navController, drawerState = drawerState, scope = scope)
            }
            composable("add_medication") {
                AddMedicationScreen(navController = navController, drawerState = drawerState, scope = scope)
            }
            composable("family") {
                FamilyScreen(navController = navController, drawerState = drawerState, scope = scope)
            }
            composable("low_stock_reminder") {
                LowStockReminderScreen(navController = navController, drawerState = drawerState, scope = scope)
            }
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