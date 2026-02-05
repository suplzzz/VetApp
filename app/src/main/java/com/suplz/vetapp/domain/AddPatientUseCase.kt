package com.suplz.vetapp.domain

import javax.inject.Inject

class AddPatientUseCase @Inject constructor(
    private val repository: VetRepository
) {

    suspend operator fun invoke(
        name: String,
        species: String,
        breed: String,
        ownerName: String,
        phoneNumber: String
    ) {
        repository.addPatient(
            name = name,
            species = species,
            breed = breed,
            ownerName = ownerName,
            phoneNumber = phoneNumber,
            createdAt = System.currentTimeMillis()
        )
    }
}