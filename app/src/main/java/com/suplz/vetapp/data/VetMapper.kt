package com.suplz.vetapp.data

import com.suplz.vetapp.domain.Patient

fun Patient.toDbModel(): PatientDbModel {
    return PatientDbModel(
        id = id,
        name = name,
        species = species,
        breed = breed,
        ownerName = ownerName,
        phoneNumber = phoneNumber,
        createdAt = createdAt
    )
}

fun PatientDbModel.toEntity(): Patient {
    return Patient(
        id = id,
        name = name,
        species = species,
        breed = breed,
        ownerName = ownerName,
        phoneNumber = phoneNumber,
        createdAt = createdAt
    )
}

fun List<PatientDbModel>.toEntities(): List<Patient> {
    return map { it.toEntity() }
}