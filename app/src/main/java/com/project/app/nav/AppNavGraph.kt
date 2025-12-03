package com.project.app.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.project.app.data.SettingsRepository
import com.project.app.view.*
import com.project.app.viewmodel.DriveViewModel
import com.project.app.viewmodel.RideViewModel
import com.project.app.viewmodel.SettingsViewModel
import com.project.app.viewmodel.UserViewModel

@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    userVM: UserViewModel,
    settingsVM: SettingsViewModel,
    rideVM: RideViewModel
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
                driveVM,
                rideVM
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
                settingsVM = settingsVM
            )

        }
        composable(Routes.Ride.routeName) {
            RideScreen(
                navController = navHostController,
                driveVM = driveVM,
                rideViewModel = rideVM,
                userName = userVM.user.value.name ?: "User"
            )
        }
    }
}
