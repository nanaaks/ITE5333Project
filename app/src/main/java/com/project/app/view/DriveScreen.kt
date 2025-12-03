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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.project.app.viewmodel.DriveViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DriveScreen(
    navController: NavController,
    driveVM: DriveViewModel
) {

    val jobs by driveVM.jobs.collectAsState(initial = emptyList())
    val acceptedJobs = jobs.filter { it.status == "Accepted" }
    val pendingJobs = jobs.filter { it.status == "Pending" }
    val totalTrips = acceptedJobs.size
    val totalEarnings = acceptedJobs.sumOf { it.price }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Drive") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(20.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Today", fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("$", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                            Spacer(Modifier.width(4.dp))
                            Text("%.2f".format(totalEarnings), fontSize = 28.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text("$totalTrips trips", fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            if (pendingJobs.isEmpty()) {
                Box(Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center) {
                    Text(text = "No requests nearby", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    itemsIndexed(pendingJobs) { idx, job ->
                        RequestBox(
                            name = job.riderName,
                            start = job.startAddress,
                            dest = job.endAddress,
                            price = job.price,
                            eta = job.rideDuration,
                            payment = job.payment,
                            rideOption = job.rideOption,
                            onAccept = { driveVM.acceptJob(job.id) },
                            onDecline = { driveVM.declineJob(job.id) },
                            isAlternate = idx % 2 == 1
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun RequestBox(
    name: String,
    start: String,
    dest: String,
    price: Double,
    eta: String,
    payment: String,
    rideOption: String,
    onAccept: () -> Unit,
    onDecline: () -> Unit,
    isAlternate: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, if (isAlternate) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.outlineVariant)
            .background(if (isAlternate) MaterialTheme.colorScheme.surfaceVariant
            else MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        Column {
            Row {
                Text(
                    name, Modifier.weight(1f), fontWeight = FontWeight.SemiBold
                )
                Icon(Icons.Default.Place, contentDescription = null, tint = Color(0xFF039BE5))
            }
            Spacer(Modifier.height(4.dp))
            Text("From: $start", color = MaterialTheme.colorScheme.onSurface)
            Text("To: $dest", color = MaterialTheme.colorScheme.onSurface)

            Text(
                "Type: $rideOption | Payment: $payment",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(4.dp))
            Row {
                Text("$${"%.2f".format(price)}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.width(16.dp))
                Icon(Icons.Default.AccessTime, contentDescription = null, tint = Color(0xFF00897B))
                Text(eta, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            }

            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = onAccept,
                    Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF43A047),
                        contentColor = MaterialTheme.colorScheme.onPrimary)
                ) { Text("Accept") }
                OutlinedButton(
                    onClick = onDecline,
                    Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFD32F2F)
                    )
                ) { Text("Decline") }
            }
        }
    }
}