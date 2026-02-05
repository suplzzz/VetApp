package com.suplz.vetapp.data

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
        val patientDbModel = PatientDbModel(
            id = 0,
            name = name,
            species = species,
            breed = breed,
            ownerName = ownerName,
            phoneNumber = phoneNumber,
            createdAt = createdAt
        )
        vetDao.addPatient(patientDbModel)
    }

    override suspend fun deletePatient(patientId: Int) {
        vetDao.deletePatient(patientId)
    }

    override suspend fun editPatient(patient: Patient) {
        vetDao.addPatient(patient.toDbModel())
    }

    override fun getPatientList(): Flow<List<Patient>> {
        return vetDao.getAllPatients().map { list ->
            list.toEntities()
        }
    }

    override suspend fun getPatient(patientId: Int): Patient {
        return vetDao.getPatient(patientId)?.toEntity()
            ?: throw RuntimeException("Patient with id $patientId not found")
    }

    override fun searchPatients(query: String): Flow<List<Patient>> {
        return vetDao.searchPatients(query).map { list ->
            list.toEntities()
        }
    }
}