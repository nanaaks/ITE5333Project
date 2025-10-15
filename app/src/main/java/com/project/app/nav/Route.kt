package com.project.app.nav

sealed class Route (val routeName: String) {

    //object SignUp : Route("signup")
    object SignIn : Route("signin")
    object Home : Route("home")
    object Drive : Route("drive")
    object Ride : Route("ride")
}