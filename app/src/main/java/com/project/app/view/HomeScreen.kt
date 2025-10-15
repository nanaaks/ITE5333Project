package com.project.app.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.project.app.viewmodel.UserViewModel
import com.project.app.nav.Route

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen (
    navController: NavController,
    userViewModel: UserViewModel
) {
    var showMenu by remember { mutableStateOf(false) }

    val user by userViewModel.user.collectAsState()

    Scaffold (
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Home") },
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
                        onDismissRequest = {showMenu = false}
                    ) {
                        DropdownMenuItem(
                            text = { Text("Profile")},
                            leadingIcon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
                            onClick = {
                                showMenu = false

                                //navigate to Profile Screen
                                //navController.navigate(Route.Profile.routeName)
                            }
                        )//DropdownMenuItem

                        DropdownMenuItem(
                            text = { Text("Account")},
                            onClick = {
                                showMenu = false
                                //navigate to Account Screen and supply the value for accountType parameter for the route
                                //navController.navigate(Route.Account.createRoute(accountType = "Premium"))
                            }
                        )//DropdownMenuItem

                        DropdownMenuItem(
                            text = { Text("Logout")},
                            onClick = {
                                showMenu = false

//                                navigate to Sign In screen
                                navController.navigate(Route.SignIn.routeName) {
                                    popUpTo(0) {
                                        // reset entire navigation and leave only SignIn screen
                                        inclusive = true
                                    }
                                }
                            }
                        )//DropdownMenuItem
                    }//DropdownMenu
                }
            )
        }//topBar
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Book a Ride",
                color = Color.Black,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    //Navigate to RideScreen
                    //navController.navigate(Route.Ride.routeName)
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.DarkGray,
                    contentColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
            ) {
                Text("Order Rides", style = MaterialTheme.typography.bodyLarge)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Search for Rides",
                color = Color.Black,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
            )

            Button(
                onClick = {
                    //Navigate to DriveScreen
                    //navController.navigate(Route.Drive.routeName)
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.DarkGray,
                    contentColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
            ) {
                Text("Select Rides", style = MaterialTheme.typography.bodyLarge)
            }

            Spacer(modifier = Modifier.height(16.dp))

        }//Column
    }
}