package com.project.app.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.project.app.viewmodel.UserViewModel
import com.project.app.view.SignInScreen
import com.project.app.view.HomeScreen
import com.project.app.view.DriveScreen
import com.project.app.view.RideScreen

@Composable
fun NavGraph (
    navController: NavHostController,
    userViewModel: UserViewModel
) {
    NavHost(
        navController,
        startDestination = Route.SignIn.routeName
    ) {
        composable(Route.SignIn.routeName) {
            SignInScreen(navController, userViewModel)
        }

        composable(Route.Home.routeName) {
            HomeScreen(navController, userViewModel)
        }
    }
}