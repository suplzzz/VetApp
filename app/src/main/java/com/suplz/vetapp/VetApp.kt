package com.suplz.vetapp

import android.app.Application
import com.suplz.vetapp.domain.VetRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class VetApp : Application() {

    @Inject
    lateinit var repository: VetRepository

    override fun onCreate() {
        super.onCreate()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val doctors = repository.getAllDoctors().first()

                if (doctors.isEmpty()) {
                    fillMockData()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private suspend fun fillMockData() {
        repository.addDoctor("Айболит Иван", "Хирург")
        repository.addDoctor("Дулиттл Джон", "Терапевт")
        repository.addDoctor("Хаус Грегори", "Диагност")

        val doctorId1 = 1
        val doctorId2 = 2

        val time = System.currentTimeMillis()

        repository.addPatient(
            name = "Барсик", species = "Кошка", breed = "Мейн-кун",
            ownerName = "Анна Иванова", phoneNumber = "89001112233", createdAt = time
        )

        repository.addPatient(
            name = "Рекс", species = "Собака", breed = "Овчарка",
            ownerName = "Петр Петров", phoneNumber = "89005556677", createdAt = time
        )

        repository.addPatient(
            name = "Кеша", species = "Попугай", breed = "Ара",
            ownerName = "Мария Сидорова", phoneNumber = "89009998877", createdAt = time
        )

        repository.addAppointment(
            patientId = 1,
            doctorId = doctorId1,
            diagnosis = "Ушиб лапы",
            prescription = "Покой и витамины",
            timestamp = time
        )

        repository.addAppointment(
            patientId = 2,
            doctorId = doctorId2,
            diagnosis = "Плановый осмотр",
            prescription = "Здоров, можно в космос",
            timestamp = time
        )
    }
}