package com.suplz.vetapp.presentation.screens.doctors

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.suplz.vetapp.presentation.screens.creation.ValidatedVetTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDoctorScreen(
    viewModel: AddDoctorViewModel = hiltViewModel(),
    onFinished: () -> Unit
) {
    val state = viewModel.state.collectAsState().value

    when (state) {
        is AddDoctorState.Input -> {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Новый врач", fontWeight = FontWeight.Bold) },
                        navigationIcon = {
                            Icon(
                                modifier = Modifier.padding(horizontal = 12.dp).clickable { viewModel.processCommand(AddDoctorCommand.Back) },
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back"
                            )
                        }
                    )
                }
            ) { innerPadding ->
                Column(modifier = Modifier.padding(innerPadding).padding(16.dp)) {
                    ValidatedVetTextField(
                        value = state.name,
                        label = "ФИО Врача",
                        onValueChange = { viewModel.processCommand(AddDoctorCommand.InputName(it)) }
                    )
                    ValidatedVetTextField(
                        value = state.specialty,
                        label = "Специальность",
                        onValueChange = { viewModel.processCommand(AddDoctorCommand.InputSpecialty(it)) }
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { viewModel.processCommand(AddDoctorCommand.Save) },
                        enabled = state.isSaveEnabled
                    ) { Text("Сохранить") }
                }
            }
        }
        AddDoctorState.Finished -> LaunchedEffect(Unit) { onFinished() }
    }
}