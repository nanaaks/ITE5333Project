package com.project.app.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.project.app.view.*
import com.project.app.viewmodel.UserViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    userVM : UserViewModel,
    toggleColorScheme : () -> Unit
) {
    val tabNavController = rememberNavController()

    NavHost(
        navController,
        startDestination = Routes.SignIn.routeName
    ) {
        composable(Routes.SignIn.routeName) {
            SignInScreen(navController, userVM)
        }

        composable(Routes.SignUp.routeName) {
            SignUpScreen(navController, userVM)
        }

        composable(Routes.Tabs.routeName) {
            HomeScreen(
                navController,
                tabNavController,
                userVM
            )
        }

        composable(Routes.Account.routeName) {
            AccountScreen(userVM)
        }

        composable(Routes.Result.routeName) {
            ResultScreen(navController)
        }

        composable(Routes.Settings.routeName) {
            SettingsScreen(toggleColorScheme)
        }
    }
}
