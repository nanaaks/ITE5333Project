package com.project.app.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.project.app.view.*
import com.project.app.viewmodel.UserViewModel

@Composable
fun RiderNavGraph(
    navController: NavHostController,
    userViewModel: UserViewModel
) {
    NavHost(
        navController = navController,
        startDestination = RiderTab.Home.routeName
    ) {

        composable(RiderTab.Home.routeName) {
            RideScreen(navController)
        }

        composable(RiderTab.Profile.routeName) {
            AccountScreen(userViewModel = userViewModel)
        }

        composable(RiderTab.Settings.routeName) {
            SettingsScreen(toggleColorScheme = {})
        }

        composable(Route.Result.routeName) {
            ResultScreen(navController)
        }
    }
}

