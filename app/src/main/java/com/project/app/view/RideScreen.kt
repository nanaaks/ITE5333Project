package com.project.app.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.project.app.nav.RiderNavGraph
import com.project.app.nav.RiderTab
import com.project.app.nav.Route
import com.project.app.viewmodel.RideViewModel
import com.project.app.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RideScreen(
    navController: NavController,
    rideViewModel: RideViewModel = viewModel()
) {
    var showAddAddressDialog by remember { mutableStateOf(false) }
    var street by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Book a Ride") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1E1E1E),
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

            /** PICKUP ADDRESS **/
            item {
                Text("Pickup Address", fontWeight = FontWeight.Bold)

                AddressDropdown(
                    label = "Select Pickup",
                    selectedAddress = rideViewModel.pickup.value,
                    addresses = rideViewModel.allAddresses.value,
                    onAddressSelected = { rideViewModel.pickup.value = it }
                )

                TextButton(
                    onClick = { showAddAddressDialog = true },
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Address")
                    Spacer(Modifier.width(6.dp))
                    Text("Add New Address")
                }
            }

            /** DESTINATION ADDRESS **/
            item {
                Text("Destination Address", fontWeight = FontWeight.Bold)

                AddressDropdown(
                    label = "Select Destination",
                    selectedAddress = rideViewModel.destination.value,
                    addresses = rideViewModel.allAddresses.value,
                    onAddressSelected = { rideViewModel.destination.value = it }
                )
            }

            /** RIDE OPTIONS **/
            item {
                Text("Ride Options", fontWeight = FontWeight.Bold)

                rideViewModel.rideOptions.forEach { option ->
                    val selected = rideViewModel.selectedRide.value == option
                    OutlinedButton(
                        onClick = { rideViewModel.selectedRide.value = option },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (selected) Color(0xFFBBDEFB) else Color.Transparent
                        )
                    ) {
                        Text("${option.name} - Base $${option.baseFare}")
                    }
                }
            }

            /** PROMO & PAYMENT **/
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
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
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

            /** CALCULATE FARE **/
            item {
                Button(
                    onClick = { rideViewModel.calculateFare() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E1E1E))
                ) {
                    Text("Calculate Fare", color = Color.White)
                }

                if (rideViewModel.fare.value > 0) {
                    Text(
                        text = "Fare: $${"%.2f".format(rideViewModel.fare.value)} | ETA: ${rideViewModel.eta.value}",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32),
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    if (rideViewModel.discountApplied.value)
                        Text("Promo Applied!", color = Color.Blue)
                }
            }

            /** BOOK BUTTON **/
            item {
                Button(
                    onClick = {
                        if (rideViewModel.pickup.value != null && rideViewModel.destination.value != null) {
                            navController.navigate(Route.Result.routeName)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                ) {
                    Text("Book Ride", color = Color.White)
                }
            }
        }
    }

    /** ADD NEW ADDRESS DIALOG **/
    if (showAddAddressDialog) {
        AlertDialog(
            onDismissRequest = { showAddAddressDialog = false },
            title = { Text("Add New Address") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = street,
                        onValueChange = { street = it },
                        label = { Text("Street Name") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                    )
                    OutlinedTextField(
                        value = city,
                        onValueChange = { city = it },
                        label = { Text("City") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (street.isNotBlank() && city.isNotBlank()) {
                        rideViewModel.addAddress(street, city)
                        street = ""
                        city = ""
                        showAddAddressDialog = false
                    }
                }) { Text("Add") }
            },
            dismissButton = {
                TextButton(onClick = { showAddAddressDialog = false }) { Text("Cancel") }
            }
        )
    }
}

///** ADDRESS DROPDOWN COMPONENT **/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressDropdown(
    label: String,
    selectedAddress: com.project.app.data.Address?,
    addresses: List<com.project.app.data.Address>,
    onAddressSelected: (com.project.app.data.Address) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedAddress?.let { "${it.street}, ${it.city}" } ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            leadingIcon = {
                Icon(
                    imageVector = if (label.contains("Pickup")) Icons.Default.LocationOn else Icons.Default.Place,
                    contentDescription = null
                )
            },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )

        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            addresses.forEach { address ->
                DropdownMenuItem(
                    text = { Text("${address.street}, ${address.city}") },
                    onClick = {
                        onAddressSelected(address)
                        expanded = false
                    }
                )
            }
        }
    }
}

