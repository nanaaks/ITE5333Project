package com.project.app.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.project.app.view.HomeTabScreen
import com.project.app.view.RideScreen
import com.project.app.view.DriveScreen
import com.project.app.viewmodel.UserViewModel

@Composable
fun TabNavGraph(
    navController: NavHostController,
    tabNavController: NavHostController,
    userVM : UserViewModel
) {
    NavHost(
        tabNavController,
        startDestination = TabRoutes.Home.routeName
    ) {
        composable(TabRoutes.Home.routeName) {
            HomeTabScreen(navController, tabNavController, userVM)
        }

        composable(TabRoutes.Ride.routeName) {
            RideScreen(navController)
        }

        composable(TabRoutes.Drive.routeName) {
            DriveScreen(navController)
        }
    }
}