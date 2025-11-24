package com.project.app.data

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.userDataStore by preferencesDataStore(name = "user_prefs")

class UserDataStore {
    companion object {
        //val USERNAME_KEY =
        //val EMAIL_KEY =
        //val PAYMENT_KEY =
        val HOME_ADDRESS_KEY = stringPreferencesKey("homeAddress")
        val WORK_ADDRESS_KEY = stringPreferencesKey("workAddress")
        val SCHOOL_ADDRESS_KEY = stringPreferencesKey("schoolAddress")
    }
}