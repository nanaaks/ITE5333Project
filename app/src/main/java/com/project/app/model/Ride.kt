package com.project.app.model

data class Ride(
    var startAddress: String = "",
    var endAddress: String = "",
    var rideDuration: String = "",
    var price: Double = 0.0,
    var payment: String = ""
)