package com.project.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.app.data.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository
): ViewModel() {

    private val _settingsData = MutableStateFlow(SettingsRepository.SettingsState())
    val settingsData: StateFlow<SettingsRepository.SettingsState> = _settingsData

    init {
        viewModelScope.launch {
            settingsRepository.settingsFlow.collectLatest { _settingsData.value = it }
        }
    }

    fun enableDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.enableDarkMode(enabled)
        }
    }

    fun enableNotifications(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.enableNotifications(enabled)
        }
    }

    fun saveSettings(
        darkMode: Boolean,
        notify: Boolean
    ) {
        viewModelScope.launch { settingsRepository.saveSettings(darkMode, notify) }
    }

    fun resetSettings() {
        viewModelScope.launch { settingsRepository.resetSettings() }
    }
}