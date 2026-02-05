package com.suplz.vetapp.domain

data class Patient(
    val id: Int,
    val name: String,
    val species: String,
    val breed: String,
    val ownerName: String,
    val phoneNumber: String,
    val createdAt: Long
)