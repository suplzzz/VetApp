package com.suplz.vetapp.domain

import javax.inject.Inject

class GetPatientUseCase @Inject constructor(
    private val repository: VetRepository
) {

    suspend operator fun invoke(patientId: Int): Patient {
        return repository.getPatient(patientId)
    }
}