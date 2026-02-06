package com.suplz.vetapp.presentation.screens.doctors

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.suplz.vetapp.domain.Doctor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorsScreen(
    modifier: Modifier = Modifier,
    viewModel: DoctorsViewModel = hiltViewModel(),
    onAddDoctorClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val state = viewModel.state.collectAsState().value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Врачи клиники") },
                navigationIcon = {
                    Icon(
                        modifier = Modifier.padding(horizontal = 12.dp).clickable { onBackClick() },
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back"
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddDoctorClick,
                containerColor = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            ) { Icon(Icons.Default.Add, contentDescription = null) }
        }
    ) { innerPadding ->
        LazyColumn(contentPadding = innerPadding) {
            items(state.doctors) { doctor ->
                DoctorCard(
                    doctor = doctor,
                    onDeleteClick = { viewModel.processCommand(DoctorsCommand.DeleteDoctor(doctor.id)) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
fun DoctorCard(
    doctor: Doctor,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = doctor.fullName, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text = doctor.specialty, fontSize = 14.sp, color = MaterialTheme.colorScheme.primary)
        }
        IconButton(onClick = onDeleteClick) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = "Delete Doctor",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}