package com.project.app.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class RiderTab(
    val routeName: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : RiderTab(
        routeName = Route.Home.routeName,
        title = "Home",
        icon = Icons.Default.Home
    )

    object Profile : RiderTab(
        routeName = Route.Profile.routeName,
        title = "Profile",
        icon = Icons.Default.Person
    )

    object Settings : RiderTab(
        routeName = Route.Settings.routeName,
        title = "Settings",
        icon = Icons.Default.Settings
    )
}
