package com.suplz.vetapp.data

import com.suplz.vetapp.domain.Appointment
import com.suplz.vetapp.domain.Doctor
import com.suplz.vetapp.domain.Patient

fun Patient.toDbModel(): PatientDbModel {
    return PatientDbModel(id, name, species, breed, ownerName, phoneNumber, createdAt)
}

fun PatientDbModel.toEntity(): Patient {
    return Patient(id, name, species, breed, ownerName, phoneNumber, createdAt)
}

fun List<PatientDbModel>.toEntities(): List<Patient> {
    return map { it.toEntity() }
}

fun DoctorDbModel.toEntity(): Doctor {
    return Doctor(id, fullName, specialty)
}

fun List<DoctorDbModel>.toDoctorEntities(): List<Doctor> {
    return map { it.toEntity() }
}

fun AppointmentWithDoctorTuple.toEntity(): Appointment {
    return Appointment(
        id = appointment.id,
        patientId = appointment.patientId,
        doctorName = doctor.fullName,
        doctorSpecialty = doctor.specialty,
        appointmentTime = appointment.appointmentTime,
        diagnosis = appointment.diagnosis,
        prescription = appointment.prescription
    )
}

fun List<AppointmentWithDoctorTuple>.toAppointmentEntities(): List<Appointment> {
    return map { it.toEntity() }
}