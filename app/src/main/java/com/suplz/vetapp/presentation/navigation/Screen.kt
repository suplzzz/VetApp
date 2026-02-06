package com.suplz.vetapp.presentation.navigation

import android.os.Bundle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(val route: String, val arguments: List<NamedNavArgument> = emptyList()) {

    data object Patients : Screen("patients")
    data object AddPatient : Screen("add_patient")
    data object Doctors : Screen("doctors")
    data object AddDoctor : Screen("add_doctor")

    data object EditPatient : Screen(
        route = "edit_patient/{patient_id}",
        arguments = listOf(navArgument("patient_id") { type = NavType.IntType })
    ) {
        fun createRoute(patientId: Int) = "edit_patient/$patientId"
        fun getPatientId(args: Bundle?) = args?.getInt("patient_id") ?: 0
    }

    data object AddAppointment : Screen(
        route = "add_appointment/{patient_id}",
        arguments = listOf(navArgument("patient_id") { type = NavType.IntType })
    ) {
        fun createRoute(patientId: Int) = "add_appointment/$patientId"
        fun getPatientId(args: Bundle?) = args?.getInt("patient_id") ?: 0
    }
}