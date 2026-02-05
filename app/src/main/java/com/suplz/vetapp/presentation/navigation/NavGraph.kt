package com.suplz.vetapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.suplz.vetapp.presentation.screens.creation.AddPatientScreen
import com.suplz.vetapp.presentation.screens.editing.EditPatientScreen
import com.suplz.vetapp.presentation.screens.patients.PatientsScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Patients.route
    ) {
        composable(Screen.Patients.route) {
            PatientsScreen(
                onPatientClick = { patient ->
                    navController.navigate(Screen.EditPatient.createRoute(patient.id))
                },
                onAddPatientClick = {
                    navController.navigate(Screen.AddPatient.route)
                }
            )
        }

        composable(Screen.AddPatient.route) {
            AddPatientScreen(
                onFinished = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.EditPatient.route,
            arguments = Screen.EditPatient.arguments
        ) { backStackEntry ->
            val patientId = Screen.EditPatient.getPatientId(backStackEntry.arguments)
            EditPatientScreen(
                patientId = patientId,
                onFinished = {
                    navController.popBackStack()
                }
            )
        }
    }
}