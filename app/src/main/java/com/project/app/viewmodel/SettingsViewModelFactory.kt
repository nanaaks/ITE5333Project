package com.project.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.app.data.SettingsRepository


class SettingsViewModelFactory(
    private val settingsRepo: SettingsRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)){
            return SettingsViewModel(settingsRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}