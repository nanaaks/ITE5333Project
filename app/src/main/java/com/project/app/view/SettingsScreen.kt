@file:OptIn(ExperimentalMaterial3Api::class)

package com.project.app.view

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.project.app.data.SettingsRepository
import com.project.app.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    settingsVM: SettingsViewModel,
    navHostController: NavHostController
) {
    val state by settingsVM.settingsData.collectAsState()

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = { navHostController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Dark Mode: ${if (state.darkMode) "ON" else "OFF"}",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontSize = 24.sp,
                    )
                )
                Switch(
                    checked = state.darkMode,
                    onCheckedChange = {
                        settingsVM.enableDarkMode(!state.darkMode)
                    }
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Notifications: ${if (state.notify) "ON" else "OFF"}",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontSize = 24.sp,
                    )
                )
                Switch(
                    checked = state.notify,
                    onCheckedChange = {
                        settingsVM.enableNotifications(!state.notify)
                    }
                )
            }
            Button(
                onClick = {
                    settingsVM.saveSettings(state.darkMode, state.notify)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Save", style = MaterialTheme.typography.bodyLarge)
            }
            Button(
                onClick = {
                    settingsVM.resetSettings()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                Text("Reset", style = MaterialTheme.typography.bodyLarge)
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    //if the bottom sheet is not already visible then show it
                    if (!sheetState.isVisible) {
                        showBottomSheet = true //show the bottom sheet
                    } else {
                        showBottomSheet = false
                    }
                }) {
                Text("About App",style = MaterialTheme.typography.bodyLarge)
            }//Button

            Spacer(modifier = Modifier.height(16.dp))
            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = { showBottomSheet = false },
                    sheetState = sheetState
                ) {
                    BottomSheetContent(
                        icon = Icons.Default.Info,
                        title = "App Information",
                        message = "App Name: PickApp\n" +
                                "Developer Name: Group1\n" +
                                "App Version: 0.04"
                    )//BottomSheetContent
                }//ModalBottomSheet
            }//if
        }// Column
    }//innerPadding
}

@Composable
fun BottomSheetContent(
    icon : ImageVector,
    title : String,
    message : String
){
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(
                horizontal = 20.dp,
                vertical = 20.dp
            )
    ){
        Row (
            modifier = Modifier.fillMaxWidth()
        ){
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column (modifier = Modifier.fillMaxWidth()){
                Text(title, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)

                Spacer(modifier = Modifier.width(8.dp))

                Text(message, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
            }//Column
        }//Row
    }//Column
}//BottomSheetContent