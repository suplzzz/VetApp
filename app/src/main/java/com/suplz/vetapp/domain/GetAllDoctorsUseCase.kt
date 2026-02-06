package com.suplz.vetapp.domain

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllDoctorsUseCase @Inject constructor(
    private val repository: VetRepository
) {
    operator fun invoke(): Flow<List<Doctor>> {
        return repository.getAllDoctors()
    }
}