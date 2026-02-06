package com.suplz.vetapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        PatientDbModel::class,
        DoctorDbModel::class,
        AppointmentDbModel::class
    ],
    version = 5,
    exportSchema = false
)
abstract class VetDatabase : RoomDatabase() {

    abstract fun vetDao(): VetDao
}