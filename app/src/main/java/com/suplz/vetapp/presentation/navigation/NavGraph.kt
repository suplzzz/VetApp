package com.suplz.vetapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.suplz.vetapp.presentation.screens.appointments.AddAppointmentScreen
import com.suplz.vetapp.presentation.screens.creation.AddPatientScreen
import com.suplz.vetapp.presentation.screens.doctors.AddDoctorScreen
import com.suplz.vetapp.presentation.screens.doctors.DoctorsScreen
import com.suplz.vetapp.presentation.screens.editing.EditPatientScreen
import com.suplz.vetapp.presentation.screens.patients.PatientsScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Patients.route) {
        composable(Screen.Patients.route) {
            PatientsScreen(
                onPatientClick = { navController.navigate(Screen.EditPatient.createRoute(it.id)) },
                onAddPatientClick = { navController.navigate(Screen.AddPatient.route) },
                onDoctorsClick = { navController.navigate(Screen.Doctors.route) }
            )
        }
        composable(Screen.AddPatient.route) {
            AddPatientScreen(onFinished = { navController.popBackStack() })
        }
        composable(Screen.EditPatient.route, arguments = Screen.EditPatient.arguments) {
            val id = Screen.EditPatient.getPatientId(it.arguments)
            EditPatientScreen(
                patientId = id,
                onAddAppointmentClick = { navController.navigate(Screen.AddAppointment.createRoute(id)) },
                onFinished = { navController.popBackStack() }
            )
        }
        composable(Screen.Doctors.route) {
            DoctorsScreen(
                onAddDoctorClick = { navController.navigate(Screen.AddDoctor.route) },
                onBackClick = { navController.popBackStack() }
            )
        }
        composable(Screen.AddDoctor.route) {
            AddDoctorScreen(onFinished = { navController.popBackStack() })
        }
        composable(Screen.AddAppointment.route, arguments = Screen.AddAppointment.arguments) {
            val id = Screen.AddAppointment.getPatientId(it.arguments)
            AddAppointmentScreen(patientId = id, onFinished = { navController.popBackStack() })
        }
    }
}