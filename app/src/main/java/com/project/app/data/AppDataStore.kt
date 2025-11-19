package com.project.app.data

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.appDataStore by preferencesDataStore("app_prefs")

class AppDataStore() {
}