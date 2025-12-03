package com.project.app.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val userId : Int = 0,
    var name: String = "",
    var email: String = "",
    var password: String = "",
    var phone: String = "",
    var payment: String = ""
)
