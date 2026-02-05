package com.suplz.vetapp.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPatientListUseCase @Inject constructor(
    private val repository: VetRepository
) {

    operator fun invoke(): Flow<List<Patient>> {
        return repository.getPatientList()
    }
}