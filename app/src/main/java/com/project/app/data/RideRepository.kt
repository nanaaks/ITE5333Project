package com.project.app.data

import com.project.app.model.Booking

object RideRepository {
    private val bookings = mutableListOf<Booking>()

    // add a new booking
    fun addBooking(booking: Booking) {
        bookings.add(booking)
    }

    fun getPendingJobs(): List<Booking> = bookings.filter { it.status == "Pending" }.toList()

    // update booking status
    fun updateJobStatus(id: String, newStatus: String) {
        val idx = bookings.indexOfFirst { it.id == id }
        if (idx != -1) {
            val old = bookings[idx]
            val updated = old.copy(status = newStatus)
            bookings[idx] = updated
        }
    }
    fun getAllBookings(): List<Booking> = bookings.toList()
}