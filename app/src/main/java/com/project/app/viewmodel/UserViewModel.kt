package com.project.app.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.project.app.model.User

class UserViewModel : ViewModel() {
    private val _user = MutableStateFlow(User())
    val user: StateFlow<User> = _user

    private val registeredUsers = mutableListOf<User>()

    fun registerUser(newUser: User): Boolean {
        // simple validation: no duplicate emails
        if (registeredUsers.any { it.email == newUser.email }) return false
        registeredUsers.add(newUser)
        _user.value = newUser
        return true
    }

    fun login(email: String, password: String): User? {
        val found = registeredUsers.find { it.email == email && it.password == password }
        if (found != null) _user.value = found
        return found
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
}
