package com.project.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.app.data.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.project.app.model.User
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class UserViewModel(
    private val userRepository: UserRepository
): ViewModel() {

    private val _userData = MutableStateFlow(UserRepository.UserState())
    val userData: StateFlow<UserRepository.UserState> = _userData

    init {
        viewModelScope.launch {
            userRepository.userFlow.collectLatest { _userData.value = it }
        }
    }

    private val _user = MutableStateFlow(User())
    val user: StateFlow<User> = _user

    suspend fun registerUser(newUser: User): Boolean {
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

    fun updateLogin(email: String, password: String) {
        _user.value = _user.value.copy(email = email, password = password)
    }

    fun updatePhone(phone: String) {
        _user.value = _user.value.copy(phone = phone)
    }

    fun updatePayment(payment: String) {
        _user.value = _user.value.copy(payment = payment)
    }
    fun updateUserDetails(name: String, email: String, phone: String, password: String) {
        _user.value = _user.value.copy(
            name = name,
            email = email,
            phone = phone,
            password = password
        )
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
}
