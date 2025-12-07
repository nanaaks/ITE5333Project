package com.project.app.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.app.data.Address
import com.project.app.data.BookingRepository
import com.project.app.data.RideRepository
import com.project.app.data.dummyAddresses
import com.project.app.model.Booking
import com.project.app.model.Ride
import com.project.app.model.RideOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.random.Random

class RideViewModel(
    private val rideRepository: RideRepository
) : ViewModel() {

    // Database operations
    private val _searchQuery = MutableStateFlow<String>("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    private val _allRides = MutableStateFlow<List<Ride>>(emptyList())

    val allRides: StateFlow<List<Ride>> = combine(_allRides, _searchQuery){ list, query ->
        if (query.isBlank()) list
        else list.filter { it.destStreet.contains(query, ignoreCase = true) }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = _allRides.value
    )

    private val _operationStatus = MutableStateFlow<String?>(null)
    val operationStatus: StateFlow<String?> = _operationStatus.asStateFlow()

    fun updateSearch(query : String){
        _searchQuery.value = query
    }

    fun getAllRides(userId: Int) =  viewModelScope.launch {
        try {
            rideRepository.getAllRides(userId).collect{ _allRides.value = it }
        } catch (e: Exception) {
            _operationStatus.value = "Failed to fetch ride history: ${e.message}"
        }
    }

    fun insertRide(ride: Ride) = viewModelScope.launch {
        try {
            rideRepository.insert(ride)
            _operationStatus.value = "Booking confirmed"
        } catch (e: Exception) {
            _operationStatus.value = "Failed to add booking: ${e.message}"
        }
    }

    fun updateRide(ride: Ride) = viewModelScope.launch {
        try {
            rideRepository.update(ride)
            _operationStatus.value = "Booking rescheduled successfully"
        } catch (e: Exception) {
            _operationStatus.value = "Failed to update booking: ${e.message}"
        }
    }

    fun deleteRide(ride: Ride) = viewModelScope.launch {
        try {
            rideRepository.delete(ride)
            _operationStatus.value = "Ride deleted"
        } catch (e: Exception) {
            _operationStatus.value = "Failed to delete ride: ${e.message}"
        }
    }

    fun deleteAllRides(userId: Int) =  viewModelScope.launch {
        try {
            //rideRepository.getAllRides(userId).collect{ _allRides.value = it }
            _operationStatus.value = "Ride history deleted"
        } catch (e: Exception) {
            _operationStatus.value = "Failed to delete ride history: ${e.message}"
        }
    }

    fun sortRidesByDate() = viewModelScope.launch {
        try {
            _allRides.value = _allRides.value.sortedBy { it.dateTime }
        } catch (e: Exception) {
            _operationStatus.value = "Failed to sort ride history: ${e.message}"
        }
    }

    fun sortRidesByPrice() = viewModelScope.launch {
        try {
            _allRides.value = _allRides.value.sortedBy { it.price }
        } catch (e: Exception) {
            _operationStatus.value = "Failed to sort ride history: ${e.message}"
        }
    }

    fun clearOperationStatus() {
        _operationStatus.value = null
    }

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

    // ฺBook Ride
    fun bookRide(userName: String) {
        val pickupStr = pickup.value?.let { "${it.street}, ${it.city}" } ?: ""
        val destStr = destination.value?.let { "${it.street}, ${it.city}" } ?: ""
        val rideDurationStr = eta.value
        val fareDouble = fare.value
        val paymentStr = paymentMethod.value

        val booking = Booking(
            id = UUID.randomUUID().toString(),
            riderName = userName,
            startAddress = pickupStr,
            endAddress = destStr,
            rideDuration = rideDurationStr,
            price = fareDouble,
            payment = paymentStr,
            status = "Pending",
            rideOption = selectedRide.value?.name ?: rideOptions.first().name
        )
        BookingRepository.addBooking(booking)
    }
}


