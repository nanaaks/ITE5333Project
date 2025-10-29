package com.project.app.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.project.app.data.Address
import com.project.app.data.dummyAddresses
import com.project.app.model.RideOption
import kotlin.random.Random

class RideViewModel : ViewModel() {

//    Address Management
    val allAddresses = mutableStateOf(dummyAddresses.toMutableList())
    val pickup = mutableStateOf<Address?>(null)
    val destination = mutableStateOf<Address?>(null)

//    Ride Details
    val selectedRide = mutableStateOf<RideOption?>(null)
    val fare = mutableStateOf(0.0)
    val eta = mutableStateOf("")
    val paymentMethod = mutableStateOf("Cash")

//    Promo System
    val promoCode = mutableStateOf("")
    val discountApplied = mutableStateOf(false)

    private val promoCodes = mapOf(
        "SAVE10" to 0.10,
        "WELCOME" to 0.15,
        "RIDE5" to 0.05
    )

//    Ride Options
    val rideOptions = listOf(
        RideOption("Economy", baseFare = 5.0, multiplier = 1.0),
        RideOption("Premium", baseFare = 8.0, multiplier = 1.3),
        RideOption("XL", baseFare = 10.0, multiplier = 1.6)
    )

//    Fare Calculation
    fun calculateFare() {
        if (pickup.value != null && destination.value != null) {
            val distance = Random.nextDouble(1.0, 15.0) // simulate km
            val selected = selectedRide.value ?: rideOptions.first()

            var price = (distance * 2.5 * selected.multiplier) + selected.baseFare

            val promo = promoCode.value.uppercase()
            if (promoCodes.containsKey(promo)) {
                price -= price * (promoCodes[promo] ?: 0.0)
                discountApplied.value = true
            } else {
                discountApplied.value = false
            }

            fare.value = price
            eta.value = "${(distance * 3).toInt()} mins"
        }
    }

//    Add Custom Address
    fun addAddress(street: String, city: String) {
        if (street.isNotBlank() && city.isNotBlank()) {
            val newAddress = Address(street.trim(), city.trim())
            if (!allAddresses.value.contains(newAddress)) {
                allAddresses.value.add(newAddress)
            }
        }
    }
}
