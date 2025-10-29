package com.project.app.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.project.app.nav.Route
import com.project.app.viewmodel.RideViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RideScreen(navController: NavController, rideViewModel: RideViewModel = viewModel()) {

    var newAddress by remember { mutableStateOf("") }
    var showAddAddressDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Book a Ride") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.DarkGray,
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

//            Saved Addresses
            item {
                Text("Saved Addresses", fontWeight = FontWeight.Bold)
                rideViewModel.savedAddresses.forEach { address ->
                    OutlinedButton(
                        onClick = { rideViewModel.pickupAddress.value = address },
                        modifier = Modifier.fillMaxWidth()
                    ) { Text(address) }
                }

                TextButton(onClick = { showAddAddressDialog = true }) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Add New Address")
                }
            }

//            Address Inputs
            item {
                OutlinedTextField(
                    value = rideViewModel.pickupAddress.value,
                    onValueChange = { rideViewModel.pickupAddress.value = it },
                    label = { Text("Pickup Location") },
                    leadingIcon = { Icon(Icons.Default.LocationOn, null) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = rideViewModel.destinationAddress.value,
                    onValueChange = { rideViewModel.destinationAddress.value = it },
                    label = { Text("Destination") },
                    leadingIcon = { Icon(Icons.Default.Place, null) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                IconButton(onClick = {
                    val temp = rideViewModel.pickupAddress.value
                    rideViewModel.pickupAddress.value = rideViewModel.destinationAddress.value
                    rideViewModel.destinationAddress.value = temp
                }) {
                    Icon(Icons.Default.SwapVert, "Swap Addresses")
                }
            }

//            Ride Options
            item {
                Text("Ride Options", fontWeight = FontWeight.Bold)
                rideViewModel.rideOptions.forEach { option ->
                    val selected = rideViewModel.selectedRide.value == option
                    OutlinedButton(
                        onClick = { rideViewModel.selectedRide.value = option },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (selected) Color.LightGray else Color.Transparent
                        )
                    ) {
                        Text("${option.name} - Base \$${option.baseFare}")
                    }
                }
            }

//             Promo & Payment
            item {
                OutlinedTextField(
                    value = rideViewModel.promoCode.value,
                    onValueChange = { rideViewModel.promoCode.value = it },
                    label = { Text("Promo Code (SAVE10, WELCOME, RIDE5)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(8.dp))

                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = rideViewModel.paymentMethod.value,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Payment Method") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        listOf("Cash", "Card", "Wallet").forEach { method ->
                            DropdownMenuItem(
                                text = { Text(method) },
                                onClick = {
                                    rideViewModel.paymentMethod.value = method
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

//            Fare Calculation
            item {
                Button(
                    onClick = {
                        rideViewModel.calculateFare()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                ) { Text("Calculate Fare", color = Color.White) }

                if (rideViewModel.fare.value > 0) {
                    Text(
                        text = "Fare: \$${"%.2f".format(rideViewModel.fare.value)}  |  ETA: ${rideViewModel.estimatedTime.value}",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32)
                    )
                    if (rideViewModel.discountApplied.value)
                        Text("Promo Applied!", color = Color.Blue)
                }
            }

//            Book Ride
            item {
                Button(
                    onClick = {
                        if (rideViewModel.pickupAddress.value.isBlank() ||
                            rideViewModel.destinationAddress.value.isBlank()
                        ) return@Button

                        navController.navigate(Route.Result.routeName)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                ) { Text("Book Ride", color = Color.White) }
            }
        }
    }

//    Add Address Dialog
    if (showAddAddressDialog) {
        AlertDialog(
            onDismissRequest = { showAddAddressDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    val added = rideViewModel.addSavedAddress(newAddress)
                    if (added) {
                        newAddress = ""
                        showAddAddressDialog = false
                    }
                }) { Text("Add") }
            },
            dismissButton = {
                TextButton(onClick = { showAddAddressDialog = false }) { Text("Cancel") }
            },
            title = { Text("Add New Address") },
            text = {
                OutlinedTextField(
                    value = newAddress,
                    onValueChange = { newAddress = it },
                    label = { Text("Address") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )
            }
        )
    }
}