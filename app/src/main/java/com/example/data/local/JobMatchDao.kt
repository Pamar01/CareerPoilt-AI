package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.JobMatchRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface JobMatchDao {
    @Query("SELECT * FROM job_matches ORDER BY createdAt DESC")
    fun getAllMatches(): Flow<List<JobMatchRecord>>

    @Query("SELECT * FROM job_matches WHERE id = :id LIMIT 1")
    fun getMatchById(id: Long): Flow<JobMatchRecord?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatch(match: JobMatchRecord): Long

    @Query("DELETE FROM job_matches WHERE id = :id")
    suspend fun deleteMatchById(id: Long)
}
