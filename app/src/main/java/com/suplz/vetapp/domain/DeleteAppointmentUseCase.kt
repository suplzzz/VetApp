package com.suplz.vetapp.domain

import javax.inject.Inject

class DeleteAppointmentUseCase @Inject constructor(
    private val repository: VetRepository
) {
    suspend operator fun invoke(appointmentId: Int) {
        repository.deleteAppointment(appointmentId)
    }
}