package com.project.app.nav

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.project.app.view.AccountScreen
import com.project.app.view.RideScreen
import com.project.app.view.DriveScreen
import com.project.app.view.HomeScreen
import com.project.app.viewmodel.DriveViewModel
import com.project.app.viewmodel.RideViewModel
import com.project.app.viewmodel.UserViewModel

@Composable
fun TabNavGraph(
    navHostController: NavHostController,
    tabNavController: NavHostController,
    userVM : UserViewModel,
    driveVM : DriveViewModel,
    rideVM : RideViewModel
) {
    val screens = listOf(TabRoutes.Home, TabRoutes.Ride, TabRoutes.Drive)
    val navBackStackEntry by tabNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            NavigationBar {
                screens.forEach { screen ->
                    NavigationBarItem(
                        label = { Text(screen.title) },
                        icon = {
                            Icon(
                                screen.icon,
                                contentDescription = screen.title
                            )
                            BadgedBox(
                                badge = {
                                    if (screen.badgeCount > 0) {
                                        Badge() {
                                            Text(screen.badgeCount.toString())
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    screen.icon,
                                    contentDescription = screen.title
                                )
                            }
                        },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.routeName } == true,
                        onClick = {
                            tabNavController.navigate(screen.routeName) {
                                //remove previously visited tab from the Navigation Stack
                                popUpTo(tabNavController.graph.findStartDestination().id) {
                                    //and save any data
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true //reload previously saved data
                            }
                        }
                    )
                }//NavigationBarItem
            }//NavigationBar
        }//bottomBar
    )//Scaffold
    { innerPadding ->
        NavHost(
            tabNavController,
            startDestination = TabRoutes.Home.routeName,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(TabRoutes.Home.routeName) {
                HomeScreen(navHostController, tabNavController, userVM, driveVM)
            }

            composable(TabRoutes.Ride.routeName) {
                val user = userVM.user.collectAsState().value

                RideScreen(
                    navController = navHostController,
                    driveVM = driveVM,
                    rideVM = rideVM,
                    userName = user.name,
                    userId = user.userId
                )
            }

            composable(TabRoutes.Drive.routeName) {
                DriveScreen(navHostController, driveVM)
            }
        }
    }
}