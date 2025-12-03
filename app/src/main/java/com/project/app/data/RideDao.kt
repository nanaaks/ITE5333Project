package com.project.app.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.project.app.model.Ride
import kotlinx.coroutines.flow.Flow

@Dao
interface RideDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertRide(ride: Ride)

    @Update
    suspend fun updateRide(ride: Ride)

    @Delete
    suspend fun deleteRide(ride: Ride)

    @Query("SELECT * FROM rides WHERE id LIKE :id ORDER BY dateTime")
    fun getRidesForUser(id: Int) : Flow<List<Ride>>
}