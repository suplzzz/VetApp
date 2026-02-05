package com.suplz.vetapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
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
    suspend fun addPatient(patient: PatientDbModel): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updatePatient(patient: PatientDbModel)
}