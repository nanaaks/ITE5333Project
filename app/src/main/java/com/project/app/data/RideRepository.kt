package com.project.app.data

import com.project.app.model.Ride
import kotlinx.coroutines.flow.Flow

class RideRepository(private val rideDao: RideDao) {

    fun getAllRides(userId: Int) : Flow<List<Ride>> = rideDao.getRidesForUser(userId)

    suspend fun insert(ride: Ride) = rideDao.insertRide(ride)

    suspend fun update(ride: Ride) = rideDao.updateRide(ride)

    suspend fun  delete(ride: Ride) = rideDao.deleteRide(ride)
}