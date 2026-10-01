package com.example.data.local

import androidx.room.*
import com.example.data.model.ApplicationRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface ApplicationDao {
    @Query("SELECT * FROM applications ORDER BY createdAt DESC")
    fun getAllApplications(): Flow<List<ApplicationRecord>>

    @Query("SELECT * FROM applications WHERE id = :id LIMIT 1")
    fun getApplicationById(id: Long): Flow<ApplicationRecord?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApplication(app: ApplicationRecord): Long

    @Update
    suspend fun updateApplication(app: ApplicationRecord)

    @Query("DELETE FROM applications WHERE id = :id")
    suspend fun deleteApplicationById(id: Long)
}
