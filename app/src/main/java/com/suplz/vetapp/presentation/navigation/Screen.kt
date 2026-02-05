package com.suplz.vetapp.presentation.navigation

import android.os.Bundle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(val route: String, val arguments: List<NamedNavArgument> = emptyList()) {

    data object Patients : Screen("patients")

    data object AddPatient : Screen("add_patient")

    data object EditPatient : Screen(
        route = "edit_patient/{patient_id}",
        arguments = listOf(navArgument("patient_id") {
            type = NavType.IntType
        })
    ) {
        fun createRoute(patientId: Int): String {
            return "edit_patient/$patientId"
        }

        fun getPatientId(arguments: Bundle?): Int {
            return arguments?.getInt("patient_id") ?: 0
        }
    }
}