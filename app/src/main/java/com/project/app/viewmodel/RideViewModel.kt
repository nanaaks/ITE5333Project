package com.project.app.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.project.app.model.RideOption
import kotlin.random.Random


class RideViewModel : ViewModel() {

    val pickupAddress = mutableStateOf("")
    val destinationAddress = mutableStateOf("")
    val selectedRide = mutableStateOf<RideOption?>(null)
    val fare = mutableStateOf(0.0)
    val promoCode = mutableStateOf("")
    val discountApplied = mutableStateOf(false)
    val paymentMethod = mutableStateOf("Cash")
    val estimatedTime = mutableStateOf("")

    private val promoCodes = mapOf(
        "SAVE10" to 0.10,
        "WELCOME" to 0.15,
        "RIDE5" to 0.05
    )

    val savedAddresses = mutableListOf(
        "123 Main Street",
        "45 King Avenue",
        "10 Elm Drive",
        "88 Sunset Blvd",
        "5 Maple Court"
    )

    val rideOptions = listOf(
        RideOption("Economy", 5.0, 1.0),
        RideOption("Premium", 8.0, 1.3),
        RideOption("XL", 10.0, 1.6)
    )

    fun calculateFare() {
        val distance = Random.nextInt(2, 20) // Dummy km
        val selected = selectedRide.value ?: rideOptions.first()
        var price = distance * 2.5 * selected.multiplier + selected.baseFare
        if (promoCodes.containsKey(promoCode.value.uppercase())) {
            price -= price * (promoCodes[promoCode.value.uppercase()] ?: 0.0)
            discountApplied.value = true
        }
        fare.value = price
        estimatedTime.value = "${Random.nextInt(5, 15)} min"
    }

    fun addSavedAddress(newAddress: String): Boolean {
        return if (newAddress.isNotBlank() && !savedAddresses.contains(newAddress)) {
            savedAddresses.add(newAddress)
            true
        } else false
    }
}
