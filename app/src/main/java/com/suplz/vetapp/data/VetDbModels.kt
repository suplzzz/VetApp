package com.suplz.vetapp.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "patients")
data class PatientDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val species: String,
    val breed: String,
    val ownerName: String,
    val phoneNumber: String,
    val createdAt: Long
)

@Entity(tableName = "doctors")
data class DoctorDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val fullName: String,
    val specialty: String
)

@Entity(
    tableName = "appointments",
    foreignKeys = [
        ForeignKey(
            entity = PatientDbModel::class,
            parentColumns = ["id"],
            childColumns = ["patientId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = DoctorDbModel::class,
            parentColumns = ["id"],
            childColumns = ["doctorId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class AppointmentDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val patientId: Int,
    val doctorId: Int,
    val appointmentTime: Long,
    val diagnosis: String,
    val prescription: String
)

data class AppointmentWithDoctorTuple(
    @Embedded val appointment: AppointmentDbModel,
    @Relation(
        parentColumn = "doctorId",
        entityColumn = "id"
    )
    val doctor: DoctorDbModel
)