package com.example.medhelper.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.medhelper.data.local.Medication
import com.example.medhelper.viewmodels.MedicationViewModel
import com.example.medhelper.viewmodels.FamilyViewModel
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.automirrored.filled.ExitToApp // Import AutoMirrored ExitToApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import com.example.medhelper.viewmodels.AuthViewModel // Import AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMedicationScreen(
    navController: NavController,
    drawerState: DrawerState,
    scope: CoroutineScope,
    medicationViewModel: MedicationViewModel = hiltViewModel(),
    familyViewModel: FamilyViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel() // Add AuthViewModel
) {
    var name by remember { mutableStateOf("") }
    var dosage by remember { mutableStateOf("") }
    var schedule by remember { mutableStateOf("") }
    var stockCount by remember { mutableStateOf("") }
    val familyMembers by familyViewModel.familyMembers.collectAsState()
    var selectedFamilyMember by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Medication") },
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
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Medication Name") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = dosage,
                onValueChange = { dosage = it },
                label = { Text("Dosage") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = schedule,
                onValueChange = { schedule = it },
                label = { Text("Schedule (e.g., Daily, 8 AM)") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = stockCount,
                onValueChange = { stockCount = it },
                label = { Text("Total Pill Count") },
                
                modifier = Modifier.fillMaxWidth()
            )

            // Family Member Selector
            if (familyMembers.isNotEmpty()) {
                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = familyMembers.find { it.id == selectedFamilyMember }?.name ?: "Select Family Member (Optional)",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Assign to") },
                                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(text = { Text("Me (No Family Member)") }, onClick = { 
                            selectedFamilyMember = null
                            expanded = false
                        })
                        familyMembers.forEach { member ->
                            DropdownMenuItem(text = { Text(member.name) }, onClick = {
                                selectedFamilyMember = member.id
                                expanded = false
                            })
                        }
                    }
                }
            }

            Button(
                onClick = {
                    val newMedication = Medication(
                        name = name,
                        dosage = dosage,
                        schedule = schedule,
                        stockCount = stockCount.toIntOrNull() ?: 0,
                        familyMemberId = selectedFamilyMember
                    )
                    medicationViewModel.addMedication(newMedication)
                    navController.popBackStack() // Go back to dashboard
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Medication")
            }
        }
    }
}