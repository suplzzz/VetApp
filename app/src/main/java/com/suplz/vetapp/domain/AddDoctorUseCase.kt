package com.suplz.vetapp.domain

import javax.inject.Inject

class AddDoctorUseCase @Inject constructor(
    private val repository: VetRepository
) {
    suspend operator fun invoke(fullName: String, specialty: String) {
        repository.addDoctor(fullName, specialty)
    }
}