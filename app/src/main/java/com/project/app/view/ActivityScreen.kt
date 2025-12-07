package com.project.app.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.project.app.model.Ride
import com.project.app.viewmodel.RideViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityScreen(
    navHostController: NavHostController,
    rideVM : RideViewModel,
    userId : Int
) {
    val rides: List<Ride> by rideVM.allRides.collectAsState(initial = emptyList())
    var showConfirmDialog by remember { mutableStateOf(false) }

    val operationStatus by rideVM.operationStatus.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    var selectedRide by remember { mutableStateOf<Ride?>(null) }
    val searchQuery by rideVM.searchQuery.collectAsState()

    LaunchedEffect(Unit, operationStatus) {
        rideVM.getAllRides(userId)

        operationStatus?.let { message ->
            snackbarHostState.showSnackbar(message)
            rideVM.clearOperationStatus()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Activity") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },//topBar
        content = { paddingValues ->
            Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)) {

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { rideVM.updateSearch(it)},
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    label = { Text("Search Past or Upcoming Rides")},
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search")},
                    singleLine = true
                )//OutlinedTextField

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        enabled = rides.isNotEmpty(),
                        onClick = {
                            rideVM.sortRidesByDate()
                        },

                        ) {
                        Text("Sort by Date")
                    }

                    Button(
                        enabled = rides.isNotEmpty(),
                        onClick = {
                            rideVM.sortRidesByPrice()
                        }) {
                        Text("Sort by Price")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                //Text("Scheduled Rides")

                //Spacer(modifier = Modifier.height(16.dp))

                Text("Ride History")

                Spacer(modifier = Modifier.height(16.dp))

                if (rides.isNotEmpty()) {
                    rides.forEach { ride ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                                .clickable {
                                    navHostController.navigate("updateRide/${ride.rideId}/$userId")
                                },
                            elevation = CardDefaults.cardElevation(8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = ride.endAddress,
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                    Text(
                                        text = "Origin: ${ride.startAddress}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = "Booking Date: ${ride.dateTime}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = "Price: ${ride.price}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = "Status: ${ride.status}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }//Column

                                IconButton(onClick = {
                                    selectedRide = ride
                                    showConfirmDialog = true
                                }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                                }
                            }//Row
                        }//Card

                        if (showConfirmDialog) {
                            AlertDialog(
                                onDismissRequest = { showConfirmDialog = false },
                                title = { Text("Delete Ride") },
                                text = { Text("Are you sure you want to delete this ride?") },
                                confirmButton = {
                                    TextButton(onClick = {
                                        rideVM.deleteRide(selectedRide!!)
                                        showConfirmDialog = false
                                    }) {
                                        Text("Yes")
                                    }
                                },
                                dismissButton = {
                                    TextButton(onClick = {
                                        showConfirmDialog = false
                                    }) {
                                        Text("No")
                                    }
                                }
                            )
                        }
                    }
                } else {
                    Text("You have no activity yet. Book a ride or schedule a future trip!.",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.fillMaxWidth().padding(16.dp))
                }
            }//Column
        }
    )
}