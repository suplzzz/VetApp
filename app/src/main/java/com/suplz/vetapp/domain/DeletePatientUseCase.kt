package com.suplz.vetapp.domain

import javax.inject.Inject

class DeletePatientUseCase @Inject constructor(
    private val repository: VetRepository
) {

    suspend operator fun invoke(patientId: Int) {
        repository.deletePatient(patientId)
    }
}