package com.project.app.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.project.app.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

enum class AddressType { HOME, WORK, SCHOOL }

class UserRepository(private val context: Context, private val userDao: UserDao) {

    private val TAG = "UserRepository"

    data class UserState(
        val homeAddress: String = "",
        val workAddress: String = "",
        val schoolAddress: String = ""
    )

    suspend fun registerUser(user: User): Boolean {
        val existing = userDao.getUserByEmail(user.email)
        if (existing != null) return false

        userDao.insertUser(user)
        return true
    }
    suspend fun loginUser(email: String, password: String): User? {
        return userDao.login(email, password)
    }

    fun getUserById(id: Int): Flow<User?> {
        return userDao.getUserById(id)
    }
    suspend fun addHome(address: String) {
        try {
            context.userDataStore.edit { prefs ->
                prefs[stringPreferencesKey("homeAddress")] = address
            }
        } catch (io : IOException) {
            Log.e(TAG, "addHomeAddress: Can't write address to the DataStore : $io", )
        } catch (ex : Exception) {
            Log.e(TAG, "addHomeAddress: Something went wrong while writing device name to DataStore : $ex ", )
        }
    }

    suspend fun addWork(address: String) {
        try {
            context.userDataStore.edit { prefs ->
                prefs[stringPreferencesKey("workAddress")] = address
            }
        } catch (io : IOException) {
            Log.e(TAG, "addWorkAddress: Can't write address to the DataStore : $io", )
        } catch (ex : Exception) {
            Log.e(TAG, "addWorkAddress: Something went wrong while writing device name to DataStore : $ex ", )
        }
    }

    suspend fun addSchool(address: String) {
        try {
            context.userDataStore.edit { prefs ->
                prefs[stringPreferencesKey("schoolAddress")] = address
            }
        } catch (io : IOException) {
            Log.e(TAG, "addSchoolAddress: Can't write address to the DataStore : $io", )
        } catch (ex : Exception) {
            Log.e(TAG, "addSchoolAddress: Something went wrong while writing device name to DataStore : $ex ", )
        }
    }

    suspend fun saveUserPrefs(
        homeAddress: String,
        workAddress: String,
        schoolAddress: String
    ) {
        try {
            context.userDataStore.edit { prefs ->
                prefs[stringPreferencesKey("homeAddress")] = homeAddress
                prefs[stringPreferencesKey("workAddress")] = workAddress
                prefs[stringPreferencesKey("schoolAddress")] = schoolAddress
            }
        } catch (io : IOException) {
            Log.e(TAG, "saveUserPrefs: Can't write changes to the DataStore : $io", )
        } catch (ex : Exception) {
            Log.e(TAG, "saveUserPrefs: Something went wrong while writing to DataStore : $ex ", )
        }
    }

    val userFlow: Flow<UserState> = context.userDataStore
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
                UserState(
                    homeAddress = prefs[UserDataStore.HOME_ADDRESS_KEY] ?: "",
                    workAddress = prefs[UserDataStore.WORK_ADDRESS_KEY] ?: "",
                    schoolAddress = prefs[UserDataStore.SCHOOL_ADDRESS_KEY] ?: ""
                )
            } catch (ex : Exception) {
                Log.e(TAG, "Error while converting preferences to UserState : $ex", )
                UserState(
                    homeAddress = "",
                    workAddress = "",
                    schoolAddress = ""
                )
            }
        }


    val homeAddressFlow : Flow<String> = context.userDataStore
        .data
        .catch { exception ->
            when(exception) {
                is IOException -> {
                    Log.e(TAG, "Unable to read address from the DataStore : $exception:", )
                    emit(emptyPreferences())
                }
                else -> throw exception
            }
        }
        .map { prefs ->
            try{
                prefs[UserDataStore.HOME_ADDRESS_KEY] ?: ""
            }catch (ex : Exception){
                Log.e(TAG, "Error while converting address to string : $ex", )
                ""
            }
        }

    val workAddressFlow : Flow<String> = context.userDataStore
        .data
        .catch { exception ->
            when(exception) {
                is IOException -> {
                    Log.e(TAG, "Unable to read address from the DataStore : $exception:", )
                    emit(emptyPreferences())
                }
                else -> throw exception
            }
        }
        .map { prefs ->
            try{
                prefs[UserDataStore.WORK_ADDRESS_KEY] ?: ""
            }catch (ex : Exception){
                Log.e(TAG, "Error while converting address to string : $ex", )
                ""
            }
        }

    val schoolAddressFlow : Flow<String> = context.userDataStore
        .data
        .catch { exception ->
            when(exception) {
                is IOException -> {
                    Log.e(TAG, "Unable to read address from the DataStore : $exception:", )
                    emit(emptyPreferences())
                }
                else -> throw exception
            }
        }
        .map { prefs ->
            try{
                prefs[UserDataStore.SCHOOL_ADDRESS_KEY] ?: ""
            }catch (ex : Exception){
                Log.e(TAG, "Error while converting address to string : $ex", )
                ""
            }
        }

    suspend fun setHome(address: String) {
        context.settingsDataStore.edit { prefs ->
            prefs[UserDataStore.HOME_ADDRESS_KEY] = address
        }
    }
}