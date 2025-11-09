package com.project.app.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.project.app.nav.RiderNavGraph
import com.project.app.nav.RiderTab
import com.project.app.viewmodel.UserViewModel

@OptIn( ExperimentalMaterial3Api::class)
@Composable
fun RiderTabScreen(
    userViewModel: UserViewModel,
    parentNavController: NavHostController
) {
    // Controller for inner tab navigation
    val tabNavController = rememberNavController()
    val navBackStackEntry by tabNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Define available tabs
    val tabs = listOf(
        RiderTab.Home,
        RiderTab.Profile,
        RiderTab.Settings
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Rider Dashboard") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = {
            NavigationBar {
                tabs.forEach { tab ->
                    NavigationBarItem(
                        selected = currentRoute == tab.routeName,
                        onClick = {
                            if (currentRoute != tab.routeName) {
                                tabNavController.navigate(tab.routeName) {
                                    launchSingleTop = true
                                    restoreState = true
                                    popUpTo(RiderTab.Home.routeName)
                                }
                            }
                        },
                        icon = { Icon(imageVector = tab.icon, contentDescription = tab.title) },
                        label = { Text(tab.title) }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            RiderNavGraph(
                navController = tabNavController,
                userViewModel = userViewModel
            )
        }
    }
}
