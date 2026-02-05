package com.suplz.vetapp.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchPatientsUseCase @Inject constructor(
    private val repository: VetRepository
) {

    operator fun invoke(query: String): Flow<List<Patient>> {
        return repository.searchPatients(query)
    }
}