package com.suplz.vetapp.domain

data class Appointment(
    val id: Int,
    val patientId: Int,
    val doctorName: String,
    val doctorSpecialty: String,
    val appointmentTime: Long,
    val diagnosis: String,
    val prescription: String
)