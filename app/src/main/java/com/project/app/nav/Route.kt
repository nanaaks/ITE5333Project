package com.project.app.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector


sealed class Route(val routeName: String) {
    object SignIn : Route("signin")
    object SignUp : Route("signup")

    object Home : Route("home")
    object Profile : Route("profile")
    object Settings : Route("settings")

    object Drive : Route("drive")
    object Result : Route("result")
}

sealed class TabRoutes(
    val routeName: String,
    val title : String,
    val icon: ImageVector,
    var badgeCount : Int = 0
) {

    object Home : TabRoutes(
        routeName = "home",
        title = "Home",
        icon = Icons.AutoMirrored.Filled.List,
        badgeCount = 0
    )

    object Ride : TabRoutes(
        routeName = "ride",
        title = "Ride",
        icon = Icons.Default.Search
    )

    object Drive : TabRoutes(
        routeName = "drive",
        title = "Drive",
        icon = Icons.Default.Settings
    )
}