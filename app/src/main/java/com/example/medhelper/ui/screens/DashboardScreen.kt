package com.example.medhelper.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.automirrored.filled.ExitToApp // Import AutoMirrored ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.medhelper.data.local.Medication
import com.example.medhelper.viewmodels.MedicationViewModel
import com.example.medhelper.viewmodels.AuthViewModel
import com.example.medhelper.viewmodels.FamilyViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    drawerState: DrawerState,
    scope: CoroutineScope,
    medicationViewModel: MedicationViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    familyViewModel: FamilyViewModel = hiltViewModel()
) {
    val medications by medicationViewModel.medications.collectAsState()
    val authToken by authViewModel.authToken.collectAsState()

    LaunchedEffect(Unit) {
        medicationViewModel.fetchMedications()
        familyViewModel.fetchFamilyMembers()
    }

    LaunchedEffect(authToken) {
        if (authToken == null) {
            navController.navigate("login") {
                popUpTo("dashboard") { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MedHelper Dashboard") },
                navigationIcon = {
                    IconButton(onClick = { scope.launch { drawerState.open() } }) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                    }
                },
                actions = {
                    IconButton(onClick = { authViewModel.logout() }) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout") // Use AutoMirrored ExitToApp
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("add_medication") }) {
                Icon(Icons.Default.Add, contentDescription = "Add Medication")
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .padding(16.dp)
        ) {
            Text("Your Medications", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(bottom = 16.dp))

            if (medications.isEmpty()) {
                Text("No medications added yet. Click the + button to add one.")
            } else {
                LazyColumn {
                    items(medications) { medication ->
                        MedicationItem(
                            medication = medication,
                            onDeleteClick = { medicationViewModel.deleteMedication(it) },
                            onClick = {
                                // Handle medication item click (e.g., show details, mark as taken)
                                // For now, let's just decrement stock as an example
                                medicationViewModel.decrementMedicationStock(medication.id)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MedicationItem(medication: Medication, onClick: (Medication) -> Unit, onDeleteClick: (Medication) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick(medication) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = medication.name, style = MaterialTheme.typography.titleMedium)
                Text(text = "Dosage: ${medication.dosage}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Schedule: ${medication.schedule}", style = MaterialTheme.typography.bodySmall)
                Text(text = "Stock: ${medication.stockCount}", style = MaterialTheme.typography.bodySmall)
                if (medication.stockCount < 5) { // Example threshold
                    Text("Low Stock! Refill soon.", color = MaterialTheme.colorScheme.error)
                }
            }
            IconButton(onClick = { onDeleteClick(medication) }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Medication")
            }
        }
    }
}