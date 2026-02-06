package com.suplz.vetapp.presentation.screens.appointments

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.suplz.vetapp.domain.Doctor
import com.suplz.vetapp.presentation.screens.creation.ValidatedVetTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAppointmentScreen(
    patientId: Int,
    viewModel: AddAppointmentViewModel = hiltViewModel(
        creationCallback = { factory: AddAppointmentViewModel.Factory -> factory.create(patientId) }
    ),
    onFinished: () -> Unit
) {
    val state = viewModel.state.collectAsState().value

    when (state) {
        is AddAppointmentState.Input -> {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Запись к врачу") },
                        navigationIcon = {
                            Icon(
                                modifier = Modifier.padding(horizontal = 12.dp).clickable { viewModel.processCommand(AddAppointmentCommand.Back) },
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back"
                            )
                        }
                    )
                }
            ) { innerPadding ->
                Column(modifier = Modifier.padding(innerPadding).padding(16.dp)) {
                    DoctorsDropdown(
                        doctors = state.availableDoctors,
                        selectedDoctor = state.selectedDoctor,
                        onDoctorSelected = { viewModel.processCommand(AddAppointmentCommand.SelectDoctor(it)) }
                    )
                    ValidatedVetTextField(
                        value = state.diagnosis,
                        label = "Диагноз",
                        onValueChange = { viewModel.processCommand(AddAppointmentCommand.InputDiagnosis(it)) }
                    )
                    ValidatedVetTextField(
                        value = state.prescription,
                        label = "Назначения",
                        onValueChange = { viewModel.processCommand(AddAppointmentCommand.InputPrescription(it)) }
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { viewModel.processCommand(AddAppointmentCommand.Save) },
                        enabled = state.isSaveEnabled
                    ) { Text("Сохранить запись") }
                }
            }
        }
        AddAppointmentState.Finished -> LaunchedEffect(Unit) { onFinished() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorsDropdown(
    doctors: List<Doctor>,
    selectedDoctor: Doctor?,
    onDoctorSelected: (Doctor) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
    ) {
        OutlinedTextField(
            modifier = Modifier.menuAnchor().fillMaxWidth(),
            readOnly = true,
            value = selectedDoctor?.let { "${it.fullName} (${it.specialty})" } ?: "",
            onValueChange = {},
            label = { Text("Выберите врача") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = OutlinedTextFieldDefaults.colors(),
            shape = RoundedCornerShape(12.dp)
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            doctors.forEach { doctor ->
                DropdownMenuItem(
                    text = { Text("${doctor.fullName} (${doctor.specialty})") },
                    onClick = {
                        onDoctorSelected(doctor)
                        expanded = false
                    }
                )
            }
        }
    }
}