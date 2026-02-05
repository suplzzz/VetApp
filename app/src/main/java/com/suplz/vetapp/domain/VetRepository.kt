package com.suplz.vetapp.domain

import kotlinx.coroutines.flow.Flow

interface VetRepository {

    suspend fun addPatient(
        name: String,
        species: String,
        breed: String,
        ownerName: String,
        phoneNumber: String,
        createdAt: Long
    )

    suspend fun deletePatient(patientId: Int)

    suspend fun editPatient(patient: Patient)

    fun getPatientList(): Flow<List<Patient>>

    suspend fun getPatient(patientId: Int): Patient

    fun searchPatients(query: String): Flow<List<Patient>>
}