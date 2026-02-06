package com.suplz.vetapp.domain

import javax.inject.Inject

class AddAppointmentUseCase @Inject constructor(
    private val repository: VetRepository
) {
    suspend operator fun invoke(
        patientId: Int,
        doctorId: Int,
        diagnosis: String,
        prescription: String
    ) {
        repository.addAppointment(
            patientId = patientId,
            doctorId = doctorId,
            diagnosis = diagnosis,
            prescription = prescription,
            timestamp = System.currentTimeMillis()
        )
    }
}