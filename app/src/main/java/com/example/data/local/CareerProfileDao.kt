package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.CareerProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface CareerProfileDao {
    @Query("SELECT * FROM career_profile WHERE id = 1 LIMIT 1")
    fun getProfile(): Flow<CareerProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProfile(profile: CareerProfile)
}
