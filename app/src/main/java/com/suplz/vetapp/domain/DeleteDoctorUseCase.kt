package com.suplz.vetapp.domain

import javax.inject.Inject

class DeleteDoctorUseCase @Inject constructor(
    private val repository: VetRepository
) {
    suspend operator fun invoke(doctorId: Int) {
        repository.deleteDoctor(doctorId)
    }
}