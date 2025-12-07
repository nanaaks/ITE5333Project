package com.project.app.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.project.app.data.Address

@Entity(
    tableName = "rides",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["id"],
            onDelete = ForeignKey.SET_DEFAULT)
                  ],
    indices = [Index(value = ["id"])]
    )
data class Ride(
    @PrimaryKey(autoGenerate = true) val rideId: Int = 0,
    val originStreet: String,
    val originCity: String,
    val destStreet: String,
    val destCity: String,
    val price: Double,
    val dateTime: String,
    val status: String,
    val id: Int = 1
)