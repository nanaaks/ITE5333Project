package com.project.app.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.project.app.model.User

class UserViewModel : ViewModel() {
    private val _user = MutableStateFlow(User())
    val user: StateFlow<User> = _user

    fun updateLogin(email: String, password: String){
        _user.value = _user.value.copy(email, password)
    }

    fun updatePhone(phone: String) {
        _user.value = _user.value.copy(phone)
    }

    fun updatePayment(payment: String) {
        _user.value = _user.value.copy(payment)
    }
}