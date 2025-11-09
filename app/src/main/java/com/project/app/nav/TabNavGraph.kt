package com.project.app.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.project.app.view.HomeScreen
import com.project.app.view.RideScreen
import com.project.app.view.DriveScreen
import com.project.app.view.SettingsScreen

@Composable
fun TabNavGraph(
    navHostController: NavHostController,
    toggleColorScheme: () -> Unit
) {
    NavHost(
        navHostController,
        startDestination = TabRoutes.Home.routeName
    ) {
//        composable(TabRoutes.Home.routeName) {
//            HomeScreen()
//        }
//
//        composable(TabRoutes.Ride.routeName) {
//            RideScreen()
//        }
//
//        composable(TabRoutes.Drive.routeName) {
//            DriveScreen()
//        }
    }
}