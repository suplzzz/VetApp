package com.suplz.vetapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface VetDao {

    @Query("SELECT * FROM patients ORDER BY createdAt DESC")
    fun getAllPatients(): Flow<List<PatientDbModel>>

    @Query("SELECT * FROM patients WHERE id == :patientId")
    suspend fun getPatient(patientId: Int): PatientDbModel?

    @Query(
        """
        SELECT * FROM patients 
        WHERE name LIKE '%' || :query || '%' 
        OR ownerName LIKE '%' || :query || '%' 
        ORDER BY createdAt DESC
        """
    )
    fun searchPatients(query: String): Flow<List<PatientDbModel>>

    @Query("DELETE FROM patients WHERE id == :patientId")
    suspend fun deletePatient(patientId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPatient(patient: PatientDbModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDoctor(doctor: DoctorDbModel)

    @Query("DELETE FROM doctors WHERE id == :doctorId")
    suspend fun deleteDoctor(doctorId: Int)

    @Query("SELECT * FROM doctors ORDER BY fullName ASC")
    fun getAllDoctors(): Flow<List<DoctorDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAppointment(appointment: AppointmentDbModel)

    @Query("DELETE FROM appointments WHERE id == :appointmentId")
    suspend fun deleteAppointment(appointmentId: Int)

    @Transaction
    @Query("SELECT * FROM appointments WHERE patientId == :patientId ORDER BY appointmentTime DESC")
    fun getAppointmentsForPatient(patientId: Int): Flow<List<AppointmentWithDoctorTuple>>
}