package com.project.app.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Hail
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector


sealed class Routes(val routeName: String) {

    object SignIn : Routes("signin")
    object SignUp : Routes("signup")
    object Home : Routes("home")
    object Tabs : Routes("tabs")
    object Account : Routes("account")
    object Settings : Routes("settings")
    object Result : Routes("result")
}

sealed class TabRoutes(
    val routeName: String,
    val title : String,
    val icon: ImageVector,
    var badgeCount : Int = 0
) {

    object Home : TabRoutes(
        routeName = "hometab",
        title = "Home",
        icon = Icons.Default.Home,
    )

    object Ride : TabRoutes(
        routeName = "ridetab",
        title = "Ride",
        icon = Icons.Default.Hail
    )

    object Drive : TabRoutes(
        routeName = "drivetab",
        title = "Drive",
        icon = Icons.Default.DirectionsCar,
        badgeCount = 0
    )
}