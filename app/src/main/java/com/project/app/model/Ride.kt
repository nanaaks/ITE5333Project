package com.project.app.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rides")
data class Ride(
    @PrimaryKey(autoGenerate = true) val rideId: Int = 0,
    var startAddress: String = "",
    var endAddress: String = "",
    var rideDuration: String = "",
    var price: Double = 0.0,
    var payment: String = ""
)