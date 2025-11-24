package com.project.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.app.data.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepo: SettingsRepository
): ViewModel() {

    private val _settingsData = MutableStateFlow(SettingsRepository.SettingsState())
    val settingsData: StateFlow<SettingsRepository.SettingsState> = _settingsData

    init {
        viewModelScope.launch {
            settingsRepo.settingsFlow.collectLatest { _settingsData.value = it }
        }
    }

    fun enableDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepo.enableDarkMode(enabled)
        }
    }

    fun enableNotifications(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepo.enableNotifications(enabled)
        }
    }

    fun saveSettings(
        darkMode: Boolean,
        notify: Boolean
    ) {
        viewModelScope.launch { settingsRepo.saveSettings(darkMode, notify) }
    }

    fun resetSettings() {
        viewModelScope.launch { settingsRepo.resetSettings() }
    }
}