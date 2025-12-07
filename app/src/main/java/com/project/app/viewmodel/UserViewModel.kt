package com.project.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.app.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.project.app.model.User
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.text.insert

class UserViewModel(
    private val userRepository: UserRepository
): ViewModel() {

    // Database operations

    private val _allUsers = MutableStateFlow<List<User>>(emptyList())
    val allUsers: StateFlow<List<User>> = _allUsers.asStateFlow()

    private val _operationStatus = MutableStateFlow<String?>(null)
    val operationStatus: StateFlow<String?> = _operationStatus.asStateFlow()

    private val _user = MutableStateFlow(User(0,"","","","",""))
    val user: StateFlow<User> = _user

    init {
        getAllUsers()
    }

    private fun getAllUsers() = viewModelScope.launch {
        userRepository.allUsers.collect { _allUsers.value = it }
    }

    suspend fun registerUser(newUser: User): Boolean  {
        val success = userRepository.registerUser(newUser)
        if (success) {
            _user.value = newUser
        }
        return success
    }

    fun login(email: String, password: String, onResult: (User?) -> Unit) {
        viewModelScope.launch {
            val user = userRepository.loginUser(email, password)
            if (user != null) _user.value = user
            onResult(user)
        }
    }

    fun updateUser(user: User) = viewModelScope.launch {
        try {
            userRepository.update(user)
            _operationStatus.value = "User updated successfully"
        } catch (e: Exception) {
            _operationStatus.value = "Failed to update user: ${e.message}"
        }
    }

    fun deleteUser(user: User) = viewModelScope.launch {
        try {
            userRepository.delete(user)
            _operationStatus.value = "User deleted successfully"
        } catch (e: Exception) {
            _operationStatus.value = "Failed to delete user: ${e.message}"
        }
    }

    // Datastore operations

    private val _userData = MutableStateFlow(UserRepository.UserState())
    val userData: StateFlow<UserRepository.UserState> = _userData

    init {
        viewModelScope.launch {
            userRepository.userFlow.collectLatest { _userData.value = it }
        }
    }

    fun savePrefs(
        home: String,
        work: String,
        school: String
    ) {
        viewModelScope.launch {
            userRepository.saveUserPrefs(home, work, school)
        }
    }

    fun addHome(address: String) {
        viewModelScope.launch {
            userRepository.addHome(address)
        }
    }

    fun addWork(address: String) {
        viewModelScope.launch {
            userRepository.addWork(address)
        }
    }

    fun addSchool(address: String) {
        viewModelScope.launch {
            userRepository.addSchool(address)
        }
    }

    fun clearOperationStatus() {
        _operationStatus.value = null
    }
}
