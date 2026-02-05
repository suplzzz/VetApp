package com.suplz.vetapp.presentation.screens.creation

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
fun AddPatientScreen(
    modifier: Modifier = Modifier,
    viewModel: AddPatientViewModel = hiltViewModel(),
    onFinished: () -> Unit
) {
    val state = viewModel.state.collectAsState()
    val currentState = state.value

    when (currentState) {
        is AddPatientState.Creation -> {
            Scaffold(
                modifier = modifier,
                topBar = {
                    TopAppBar(
                        title = { Text("Новый пациент", fontWeight = FontWeight.Bold) },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                        navigationIcon = {
                            Icon(
                                modifier = Modifier
                                    .padding(horizontal = 12.dp)
                                    .clickable { viewModel.processCommand(AddPatientCommand.Back) },
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back"
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
                        text = "Дата регистрации: ${DateFormatter.formatCurrentDate()}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    ValidatedVetTextField(
                        value = currentState.name,
                        label = "Кличка питомца",
                        onValueChange = { viewModel.processCommand(AddPatientCommand.InputName(it)) },
                        error = currentState.nameError
                    )
                    SpeciesDropdownMenu(
                        selectedSpecies = currentState.species,
                        onSpeciesSelected = { viewModel.processCommand(AddPatientCommand.InputSpecies(it)) },
                        error = currentState.speciesError
                    )
                    ValidatedVetTextField(
                        value = currentState.breed,
                        label = "Порода (необязательно)",
                        onValueChange = { viewModel.processCommand(AddPatientCommand.InputBreed(it)) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Данные владельца",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ValidatedVetTextField(
                        value = currentState.ownerName,
                        label = "ФИО Владельца",
                        onValueChange = { viewModel.processCommand(AddPatientCommand.InputOwner(it)) },
                        error = currentState.ownerNameError
                    )
                    ValidatedVetTextField(
                        value = currentState.phoneNumber,
                        label = "Номер телефона",
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Phone,
                            imeAction = ImeAction.Done
                        ),
                        onValueChange = { viewModel.processCommand(AddPatientCommand.InputPhone(it)) },
                        error = currentState.phoneError
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { viewModel.processCommand(AddPatientCommand.Save) },
                    ) {
                        Text("Сохранить")
                    }
                }
            }
        }
        AddPatientState.Finished -> {
            LaunchedEffect(Unit) { onFinished() }
        }
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

