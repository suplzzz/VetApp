package com.suplz.vetapp.domain

import javax.inject.Inject

class EditPatientUseCase @Inject constructor(
    private val repository: VetRepository
) {

    suspend operator fun invoke(patient: Patient) {
        repository.editPatient(patient)
    }
}