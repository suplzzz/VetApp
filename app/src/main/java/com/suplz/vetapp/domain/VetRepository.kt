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

    suspend fun addDoctor(fullName: String, specialty: String)
    suspend fun deleteDoctor(doctorId: Int)
    fun getAllDoctors(): Flow<List<Doctor>>

    suspend fun addAppointment(
        patientId: Int,
        doctorId: Int,
        diagnosis: String,
        prescription: String,
        timestamp: Long
    )
    suspend fun deleteAppointment(appointmentId: Int)
    fun getAppointmentsForPatient(patientId: Int): Flow<List<Appointment>>
}