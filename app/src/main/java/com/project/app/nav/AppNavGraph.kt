package com.project.app.nav

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.project.app.view.*
import com.project.app.viewmodel.DriveViewModel
import com.project.app.viewmodel.UserViewModel

@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    userVM: UserViewModel,
    toggleColorScheme: () -> Unit,
    isDarkMode: Boolean
) {
    val tabNavController = rememberNavController()
    val driveVM: DriveViewModel = viewModel()


    NavHost(
        navHostController,
        startDestination = Routes.SignIn.routeName
    ) {
        composable(Routes.SignIn.routeName) {
            SignInScreen(navHostController, userVM)
        }

        composable(Routes.SignUp.routeName) {
            SignUpScreen(navHostController, userVM)
        }

        composable(Routes.Tabs.routeName) {
            TabNavGraph(
                navHostController,
                tabNavController,
                userVM,
                driveVM
            )
        }

        composable(Routes.Account.routeName) {
            AccountScreen(userVM)
        }

        composable(Routes.Result.routeName) {
            ResultScreen(navHostController)
        }

        composable(Routes.Settings.routeName) {
            SettingsScreen(
                toggleColorScheme,
                isDarkMode
            )
        }
    }
}
