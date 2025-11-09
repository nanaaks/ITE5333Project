package com.project.app.nav


sealed class Route(val routeName: String) {
    object SignIn : Route("signin")
    object SignUp : Route("signup")

    object Home : Route("home")
    object Profile : Route("profile")
    object Settings : Route("settings")

    object Drive : Route("drive")
    object Result : Route("result")
}
