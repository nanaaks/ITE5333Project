package com.project.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.app.data.RideRepository

class RideViewModelFactory(
    private val rideRepository: RideRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RideViewModel::class.java)) {
            return RideViewModel(rideRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
