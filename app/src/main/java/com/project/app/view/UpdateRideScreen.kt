package com.project.app.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.project.app.model.Ride
import com.project.app.viewmodel.RideViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateRideScreen(
    navHostController: NavHostController,
    rideVM : RideViewModel,
    userId : Int,
    rideId: Int
) {
    val allRides: List<Ride> by rideVM.allRides.collectAsState(initial = emptyList())
    val ride = remember(allRides) { allRides.find { it.rideId == rideId } }

    var originStreet by remember(ride) { mutableStateOf(ride?.originStreet ?: "") }
    var originCity by remember(ride) { mutableStateOf(ride?.originCity ?: "") }
    var destStreet by remember(ride) { mutableStateOf(ride?.destStreet ?: "") }
    var destCity by remember(ride) { mutableStateOf(ride?.destCity ?: "") }
    var price by remember(ride) { mutableStateOf(ride?.price?.toString() ?: "") }
    var dateTime by remember(ride) { mutableStateOf(ride?.dateTime ?: "") }
    var status by remember(ride) { mutableStateOf(ride?.status ?: "") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Ride Details") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        content = { paddingValues ->
            Column(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp)) {

                TextField(
                    value = originStreet,
                    onValueChange = { originStreet = it },
                    label = { Text("Street") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = originCity,
                    onValueChange = { originCity = it },
                    label = { Text("City") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = destStreet,
                    onValueChange = { destStreet = it },
                    label = { Text("Destination Street") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = destCity,
                    onValueChange = { destCity = it },
                    label = { Text("Destination City") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = dateTime,
                    onValueChange = { dateTime = it },
                    label = { Text("Date/Time") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (ride != null) {
                            val updatedRide = ride.copy(
                                originStreet = originStreet,
                                originCity = originCity,
                                destStreet = destStreet,
                                destCity = destCity,
                                price = price.toDouble(),
                                dateTime = dateTime,
                                status = status
                            )
                            rideVM.updateRide(updatedRide)
                            navHostController.popBackStack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Confirm")
                }

                Button(
                    onClick = { navHostController.popBackStack() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancel")
                }
            }
        }
    )
}