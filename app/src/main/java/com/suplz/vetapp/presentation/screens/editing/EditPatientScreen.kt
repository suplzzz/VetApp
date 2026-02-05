package com.suplz.vetapp.presentation.screens.editing

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.suplz.vetapp.presentation.ui.SpeciesDropdownMenu
import com.suplz.vetapp.presentation.utils.DateFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPatientScreen(
    modifier: Modifier = Modifier,
    patientId: Int,
    viewModel: EditPatientViewModel = hiltViewModel(
        creationCallback = { factory: EditPatientViewModel.Factory ->
            factory.create(patientId)
        }
    ),
    onFinished: () -> Unit
) {
    val state = viewModel.state.collectAsState()
    val currentState = state.value

    when (currentState) {
        is EditPatientState.Editing -> {
            Scaffold(
                modifier = modifier,
                topBar = {
                    TopAppBar(
                        title = { Text("Карточка пациента", fontWeight = FontWeight.Bold) },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                        navigationIcon = {
                            Icon(
                                modifier = Modifier
                                    .padding(horizontal = 12.dp)
                                    .clickable { viewModel.processCommand(EditPatientCommand.Back) },
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
                            )
                        },
                        actions = {
                            Icon(
                                modifier = Modifier
                                    .padding(end = 16.dp)
                                    .clickable { viewModel.processCommand(EditPatientCommand.Delete) },
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = "Delete",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    )
                }
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Дата регистрации: ${DateFormatter.formatDateToString(currentState.patient.createdAt)}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    ValidatedVetTextField(
                        value = currentState.patient.name,
                        label = "Кличка",
                        onValueChange = { viewModel.processCommand(EditPatientCommand.InputName(it)) },
                        error = currentState.nameError
                    )
                    SpeciesDropdownMenu(
                        selectedSpecies = currentState.patient.species,
                        onSpeciesSelected = { viewModel.processCommand(EditPatientCommand.InputSpecies(it)) },
                        error = currentState.speciesError
                    )
                    ValidatedVetTextField(
                        value = currentState.patient.breed,
                        label = "Порода",
                        onValueChange = { viewModel.processCommand(EditPatientCommand.InputBreed(it)) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Владелец", fontWeight = FontWeight.Bold)
                    ValidatedVetTextField(
                        value = currentState.patient.ownerName,
                        label = "ФИО",
                        onValueChange = { viewModel.processCommand(EditPatientCommand.InputOwner(it)) },
                        error = currentState.ownerNameError
                    )
                    ValidatedVetTextField(
                        value = currentState.patient.phoneNumber,
                        label = "Телефон",
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Phone,
                            imeAction = ImeAction.Done
                        ),
                        onValueChange = { viewModel.processCommand(EditPatientCommand.InputPhone(it)) },
                        error = currentState.phoneError
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { viewModel.processCommand(EditPatientCommand.Save) },
                    ) {
                        Text("Сохранить изменения")
                    }
                }
            }
        }
        EditPatientState.Finished -> LaunchedEffect(Unit) { onFinished() }
        EditPatientState.Initial -> {}
    }
}

@Composable
fun ValidatedVetTextField(
    value: String,
    label: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    error: String? = null
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        keyboardOptions = keyboardOptions,
        shape = RoundedCornerShape(12.dp),
        isError = error != null,
        supportingText = {
            if (error != null) {
                Text(text = error, color = MaterialTheme.colorScheme.error)
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            errorBorderColor = MaterialTheme.colorScheme.error,
            errorLabelColor = MaterialTheme.colorScheme.error
        )
    )
}

