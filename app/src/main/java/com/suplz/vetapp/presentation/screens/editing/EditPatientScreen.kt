package com.suplz.vetapp.presentation.screens.editing

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.suplz.vetapp.domain.Appointment
import com.suplz.vetapp.presentation.screens.creation.ValidatedVetTextField
import com.suplz.vetapp.presentation.ui.SpeciesDropdownMenu
import com.suplz.vetapp.presentation.utils.DateFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPatientScreen(
    patientId: Int,
    viewModel: EditPatientViewModel = hiltViewModel(
        creationCallback = { factory: EditPatientViewModel.Factory -> factory.create(patientId) }
    ),
    onAddAppointmentClick: () -> Unit,
    onFinished: () -> Unit
) {
    val state = viewModel.state.collectAsState().value

    when (state) {
        is EditPatientState.Editing -> {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Карточка пациента", fontWeight = FontWeight.Bold) },
                        navigationIcon = {
                            Icon(
                                modifier = Modifier.padding(horizontal = 12.dp).clickable { viewModel.processCommand(EditPatientCommand.Back) },
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back"
                            )
                        },
                        actions = {
                            Icon(
                                modifier = Modifier.padding(end = 16.dp).clickable { viewModel.processCommand(EditPatientCommand.Delete) },
                                imageVector = Icons.Outlined.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error
                            )
                        }
                    )
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = onAddAppointmentClick,
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Row(modifier = Modifier.padding(horizontal = 16.dp)) {
                            Icon(Icons.Default.Add, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Новая запись")
                        }
                    }
                }
            ) { innerPadding ->
                LazyColumn(contentPadding = innerPadding) {
                    item {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Дата регистрации: ${DateFormatter.formatDateToString(state.patient.createdAt)}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.height(16.dp))
                            ValidatedVetTextField(
                                value = state.patient.name, label = "Кличка",
                                onValueChange = { viewModel.processCommand(EditPatientCommand.InputName(it)) }, error = state.nameError
                            )
                            SpeciesDropdownMenu(
                                selectedSpecies = state.patient.species,
                                onSpeciesSelected = { viewModel.processCommand(EditPatientCommand.InputSpecies(it)) }, error = state.speciesError
                            )
                            ValidatedVetTextField(
                                value = state.patient.breed, label = "Порода",
                                onValueChange = { viewModel.processCommand(EditPatientCommand.InputBreed(it)) }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Владелец", fontWeight = FontWeight.Bold)
                            ValidatedVetTextField(
                                value = state.patient.ownerName, label = "ФИО",
                                onValueChange = { viewModel.processCommand(EditPatientCommand.InputOwner(it)) }, error = state.ownerNameError
                            )
                            ValidatedVetTextField(
                                value = state.patient.phoneNumber, label = "Телефон",
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Done),
                                onValueChange = { viewModel.processCommand(EditPatientCommand.InputPhone(it)) }, error = state.phoneError
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = { viewModel.processCommand(EditPatientCommand.Save) },
                                enabled = state.isSaveEnabled
                            ) { Text("Сохранить изменения") }

                            Spacer(modifier = Modifier.height(24.dp))
                            Text("История болезней", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }

                    if (state.appointments.isEmpty()) {
                        item {
                            Text(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                text = "Записей пока нет",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        items(state.appointments) { appointment ->
                            AppointmentCard(
                                appointment = appointment,
                                onDeleteClick = { viewModel.processCommand(EditPatientCommand.DeleteAppointment(appointment.id)) },
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
        EditPatientState.Finished -> LaunchedEffect(Unit) { onFinished() }
        EditPatientState.Initial -> {}
    }
}

@Composable
fun AppointmentCard(
    appointment: Appointment,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(DateFormatter.formatDateToString(appointment.appointmentTime), style = MaterialTheme.typography.labelMedium)
                Text(appointment.doctorName, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Диагноз: ${appointment.diagnosis}", fontWeight = FontWeight.Medium)
            Text("Назначение: ${appointment.prescription}", style = MaterialTheme.typography.bodySmall)
        }
        IconButton(
            onClick = onDeleteClick,
            modifier = Modifier.size(24.dp).padding(start = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = "Delete Appointment",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}