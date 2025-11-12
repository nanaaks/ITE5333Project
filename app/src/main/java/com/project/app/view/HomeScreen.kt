package com.project.app.view

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.project.app.viewmodel.UserViewModel
import com.project.app.ui.theme.AppTheme
import com.project.app.nav.Routes
import com.project.app.nav.TabRoutes
import com.project.app.nav.TabNavGraph
import kotlin.compareTo
import kotlin.toString

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen (
    navController: NavHostController,
    tabNavController : NavHostController,
    userVM: UserViewModel
) {

    val screens = listOf(
        TabRoutes.Home,
        TabRoutes.Ride,
        TabRoutes.Drive
    )

    val user by userVM.user.collectAsState()

    var showMenu by remember { mutableStateOf(false) }

    var isDarkMode by remember { mutableStateOf(false) }

    AppTheme(darkTheme = isDarkMode) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("PickApp") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.DarkGray,
                        titleContentColor = Color.White
                    ),
                    actions = {
                        IconButton(onClick = {
                            showMenu = true
                        }) {
                            Icon(
                                Icons.Filled.MoreVert,
                                contentDescription = "Options",
                                tint = Color.White
                            )
                        }//IconButton
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Profile") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Filled.Person,
                                        contentDescription = "Profile"
                                    )
                                },
                                onClick = {
                                    showMenu = false
                                    navController.navigate(Routes.Account.routeName)
                                }
                            )//DropdownMenuItem

                            DropdownMenuItem(
                                text = { Text("Settings") },
                                onClick = {
                                    showMenu = false
                                    navController.navigate(Routes.Settings.routeName)
                                }
                            )//DropdownMenuItem

                            DropdownMenuItem(
                                text = { Text("Logout") },
                                onClick = {
                                    showMenu = false
                                    navController.navigate(Routes.SignIn.routeName) {
                                        popUpTo(0) {
                                            inclusive = true
                                        }
                                    }
                                }
                            )//DropdownMenuItem
                        }//DropdownMenu
                    }
                )
            }, //topBar

            bottomBar = {
                NavigationBar {
                    val navBackStackEntry by tabNavController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination

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
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .padding(top = 100.dp)
            ) {
                Text(
                    text = "Hello ${user.name}!",
                    color = Color.Black,
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                    textAlign = TextAlign.Start
                )

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    item {
                        Text(
                            text = "Welcome to PickApp",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )
                    }

                    item {
                        Text(
                            text = "Book a Ride or Search for Passengers",
                            color = Color.Black
                        )
                    }

                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable {
                                    //open premium sign up dialog
                                },
                            elevation = CardDefaults.cardElevation(8.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = "Enjoy bonuses with a Premium account!\n\n"
                                            + "Save on rides!\n"
                                            + "Exclusive offers!\n"
                                            +"Member-only promos!\n\n"
                                            +"See terms and benefits details",
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Spacer(modifier = Modifier.padding(8.dp))
                            }
                        }
                    }

                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable {
                                    //apply promotion to account
                                },
                            elevation = CardDefaults.cardElevation(8.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = "Earn 5% on rides\n\n"
                                            + "Get 4 weeks free",
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Spacer(modifier = Modifier.padding(8.dp))
                            }
                        }
                    }

                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable {
                                    //apply promotion to account
                                },
                            elevation = CardDefaults.cardElevation(8.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = "10% - 30% off Rides\n\n"
                                            + "Expires on 31st Nov, 2025",
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Spacer(modifier = Modifier.padding(8.dp))
                            }
                        }
                    }

                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable {
                                    //apply promotion to account
                                },
                            elevation = CardDefaults.cardElevation(8.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = "Let us know what you think!\n\n"
                                            + "Provide feedback",
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Spacer(modifier = Modifier.padding(8.dp))
                            }
                        }
                    }

                    //show promotions and updates
//                newsInfo.forEach { info ->
//                    item {
//                        InfoCard(info)
//                    }
//                }

                }//LazyColumn
            }//Column

            TabNavGraph(
                navController,
                tabNavController,
                userVM
            )//TabNavGraph
        }//Scaffold
    }//Theme
}