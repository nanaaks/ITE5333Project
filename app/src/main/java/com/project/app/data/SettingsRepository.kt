package com.project.app.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class SettingsRepository(private val context: Context) {

    private val TAG = "SettingsRepository"

    data class SettingsState(
        val darkMode: Boolean = false,
        val notify: Boolean = false
    )

    suspend fun saveSettings(
        darkMode: Boolean,
        notify: Boolean
    ) {
        try {
            context.settingsDataStore.edit { prefs ->
                prefs[booleanPreferencesKey("dark_mode")] = darkMode
                prefs[booleanPreferencesKey("notifications")] = notify
            }
        } catch (io : IOException){
            Log.e(TAG, "saveSettings: Can't write app settings to the DataStore : $io", )
        }catch (ex : Exception){
            Log.e(TAG, "saveAllPrefs: Something went wrong while writing to DataStore : $ex ", )
        }//catch
    }

    suspend fun resetSettings(){
        try{
            context.settingsDataStore.edit { prefs ->
                prefs.clear()
            }
        }catch (io : IOException){
            Log.e(TAG, "resetSettings: Can't delete the data from the DataStore : $io", )
        }catch (ex : Exception){
            Log.e(TAG, "resetSettings: Something went wrong while deleting the preferences from DataStore : $ex ", )
        }//catch
    }

    val settingsFlow: Flow<SettingsState> = context.settingsDataStore
        .data
        .catch { exception ->
            when(exception) {
                is IOException -> {
                    Log.e(TAG, "Unable to read preferences from the DataStore : $exception:", )
                    emit(emptyPreferences())
                }
                else -> throw exception
            }
        }
        .map { prefs ->
            try{
                SettingsState(
                    darkMode = prefs[SettingsDataStore.DARK_MODE_KEY] ?: false,
                    notify = prefs[SettingsDataStore.NOTIFICATIONS_KEY] ?: false
                )
            }catch (ex : Exception){
                Log.e(TAG, "Error while converting preferences to SettingsState : $ex", )
                SettingsState(
                    darkMode = false,
                    notify = false
                )
            }
        }

    suspend fun enableDarkMode(enabled: Boolean) {
        context.settingsDataStore.edit { prefs ->
            prefs[SettingsDataStore.DARK_MODE_KEY] = enabled
        }
    }

    suspend fun enableNotifications(enabled: Boolean) {
        context.settingsDataStore.edit { prefs ->
            prefs[SettingsDataStore.NOTIFICATIONS_KEY] = enabled
        }
    }
}