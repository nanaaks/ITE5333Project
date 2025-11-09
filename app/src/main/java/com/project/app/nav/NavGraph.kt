package com.project.app.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.project.app.view.*
import com.project.app.viewmodel.UserViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    userViewModel: UserViewModel,
    toggleColorScheme : () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Route.SignIn.routeName
    ) {

        composable(Route.SignIn.routeName) {
            SignInScreen(navController, userViewModel)
        }

        composable(Route.SignUp.routeName) {
            SignUpScreen(navController, userViewModel)
        }

        composable(Route.Drive.routeName) {
            DriveScreen(navController)
        }

        composable(Route.Home.routeName) {
            RiderTabScreen(
                userViewModel = userViewModel,
                parentNavController = navController
            )
        }

        composable(Route.Result.routeName) {
            ResultScreen(navController)
        }

        composable(Route.Settings.routeName) {
            SettingsScreen(toggleColorScheme)
        }
    }
}
