package com.project.app.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriveScreen(
    navController: NavController,
) {
//Mock Data
    var isOnline by remember { mutableStateOf(true) }
    val requests by remember(isOnline) { mutableStateOf(if (isOnline) {
        listOf(
            "John, 0.8 mi, Rogers Centre, $12.40, Now",
            "Sarah, 2.1 mi, Airport, $28.00, 7:30 PM")
    } else { emptyList() }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Drive") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.DarkGray,
                    titleContentColor = Color.White
                ), // title
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Text(
                            "Back",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }, // navigationIcon : Back
                actions = {
                    Row(verticalAlignment = Alignment.CenterVertically)
                    {
                        Switch(
                            checked = isOnline,
                            onCheckedChange = { isOnline = it }
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = if (isOnline) "Online" else "Offline",
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    }
                }// Switch: Online, Offline
            ) // CenterAlignedTopAppBar
        } // topBar
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(20.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "Today",
                            fontSize = 16.sp,
                            color = Color.DarkGray
                        )// Date
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                "$",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Green
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                "48.50",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold
                            ) // Earning
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            "3 trips",
                            fontSize = 16.sp,
                            color = Color.DarkGray
                        )
                    } //trips
                }
            }
            //Box: Earnings Box
            Spacer(Modifier.height(16.dp))
            if (requests.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = if (isOnline) {
                            "No requests nearby"
                        } else {
                            "Go online to receive requests"
                        },
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(requests) { str ->
                        val p = str.split(", ")
                        RequestBox(
                            name = p[0],
                            dist = p[1],
                            dest = p[2],
                            fare = p[3],
                            time = p[4],
                            scheduled = p[4] != "Now"
                        )
                    }
                } // LazyColumn
            }
        }
    }
}

@Composable
fun RequestBox(name: String, dist: String, dest: String, fare: String, time: String, scheduled: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.LightGray)
            .background(Color.White)
            .padding(16.dp)
    ) {
        Column {
            Row {
                Text("${name}",
                    Modifier.weight(1f),
                    fontWeight = FontWeight.SemiBold
                )
                Text(dist,
                    color = Color.Gray
                )
            } // Text : name & distance

            Spacer(Modifier.height(4.dp))
            Text(dest) // Text: destination

            Spacer(Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(fare,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(time,
                    fontWeight = FontWeight.Bold,
                    color = if (scheduled) Color.Green
                    else Color.Gray
                )
            }// Text: fare & time

            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp))
            {
                Button(
                    onClick = {},
                    Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(Color.DarkGray)
                ) {
                    Text("Accept")
                } // Button : Accept
                OutlinedButton(
                    onClick = {},
                    Modifier.weight(1f)
                ) {
                    Text("Decline")
                } // Button : Decline
            }
        }
    }
}
