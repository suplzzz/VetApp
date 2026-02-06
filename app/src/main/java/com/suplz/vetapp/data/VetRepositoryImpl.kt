package com.suplz.vetapp.data

import com.suplz.vetapp.domain.Appointment
import com.suplz.vetapp.domain.Doctor
import com.suplz.vetapp.domain.Patient
import com.suplz.vetapp.domain.VetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class VetRepositoryImpl @Inject constructor(
    private val vetDao: VetDao
) : VetRepository {

    override suspend fun addPatient(
        name: String,
        species: String,
        breed: String,
        ownerName: String,
        phoneNumber: String,
        createdAt: Long
    ) {
        vetDao.addPatient(
            PatientDbModel(0, name, species, breed, ownerName, phoneNumber, createdAt)
        )
    }

    override suspend fun deletePatient(patientId: Int) {
        vetDao.deletePatient(patientId)
    }

    override suspend fun editPatient(patient: Patient) {
        vetDao.addPatient(patient.toDbModel())
    }

    override fun getPatientList(): Flow<List<Patient>> {
        return vetDao.getAllPatients().map { it.toEntities() }
    }

    override suspend fun getPatient(patientId: Int): Patient {
        return vetDao.getPatient(patientId)?.toEntity()
            ?: throw RuntimeException("Patient not found")
    }

    override fun searchPatients(query: String): Flow<List<Patient>> {
        return vetDao.searchPatients(query).map { it.toEntities() }
    }

    override suspend fun addDoctor(fullName: String, specialty: String) {
        vetDao.addDoctor(DoctorDbModel(0, fullName, specialty))
    }

    override suspend fun deleteDoctor(doctorId: Int) {
        vetDao.deleteDoctor(doctorId)
    }

    override fun getAllDoctors(): Flow<List<Doctor>> {
        return vetDao.getAllDoctors().map { it.toDoctorEntities() }
    }

    override suspend fun addAppointment(
        patientId: Int,
        doctorId: Int,
        diagnosis: String,
        prescription: String,
        timestamp: Long
    ) {
        vetDao.addAppointment(
            AppointmentDbModel(0, patientId, doctorId, timestamp, diagnosis, prescription)
        )
    }

    override suspend fun deleteAppointment(appointmentId: Int) {
        vetDao.deleteAppointment(appointmentId)
    }

    override fun getAppointmentsForPatient(patientId: Int): Flow<List<Appointment>> {
        return vetDao.getAppointmentsForPatient(patientId).map { it.toAppointmentEntities() }
    }
}