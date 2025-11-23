package com.project.app.viewmodel

import androidx.lifecycle.ViewModel
import com.project.app.model.Booking
import com.project.app.data.RideRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DriveViewModel : ViewModel() {
    private val _jobs = MutableStateFlow<List<Booking>>(emptyList())
    val jobs: StateFlow<List<Booking>> = _jobs

    init {
        refreshJobs()
    }
    fun acceptJob(id: String) {
        RideRepository.updateJobStatus(id, "Accepted")
        refreshJobs()
    }
    fun declineJob(id: String) {
        RideRepository.updateJobStatus(id, "Declined")
        refreshJobs()
    }
    fun refreshJobs() {
        _jobs.value = RideRepository.getAllBookings()
    }
}