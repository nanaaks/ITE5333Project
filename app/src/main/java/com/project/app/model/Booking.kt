package com.project.app.model

data class Booking(
    val id: String,                  // job/ride id
    val riderName: String,
    val startAddress: String,
    val endAddress: String,
    val rideDuration: String,
    val price: Double,
    val payment: String,
    val rideOption: String,
    val status: String = "Pending"   // Pending, Accepted, Declined
)