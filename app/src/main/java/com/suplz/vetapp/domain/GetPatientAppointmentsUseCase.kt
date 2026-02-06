package com.suplz.vetapp.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPatientAppointmentsUseCase @Inject constructor(
    private val repository: VetRepository
) {
    operator fun invoke(patientId: Int): Flow<List<Appointment>> {
        return repository.getAppointmentsForPatient(patientId)
    }
}