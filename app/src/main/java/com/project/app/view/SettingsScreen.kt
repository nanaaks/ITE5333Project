@file:OptIn(ExperimentalMaterial3Api::class)

package com.project.app.view

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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen(
    toggleColorScheme : () -> Unit
) {
    // State Variables
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    var isDarkMode by remember { mutableStateOf(false) }
    var isEnabled by remember { mutableStateOf(false) }

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text("Settings", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        Text("Toggle Color Scheme")

        Switch(
            checked = isDarkMode,
            onCheckedChange =  { toggleColorScheme() }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Enable/Disable Notifications")

        Switch(
            checked = isEnabled,
            onCheckedChange = { isEnabled = it }
        )

        Button(modifier = Modifier.fillMaxWidth(),
            onClick = {
                //if the bottom sheet is not already visible then show it
                if (!sheetState.isVisible) {
                    showBottomSheet = true //show the bottom sheet
                }else{
                    showBottomSheet = false
                }
            }) {
            Text("About App")
        }//Button

        Spacer(modifier = Modifier.height(16.dp))

        if (showBottomSheet){

            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState
            ) {

                BottomSheetContent(
                    icon = Icons.Default.Info,
                    title = "App Information",
                    message = "Welcome to PickApp!\n" +
                            "App Name: PickApp\n" +
                            "Developer Name: Group1\n" +
                            "App Version: 0.02"
                )//BottomSheetContent
            }//ModalBottomSheet
        }//if
    }
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
                tint = Color.Blue,
                modifier = Modifier.size(30.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column (modifier = Modifier.fillMaxWidth()){
                Text(title,
                    style = MaterialTheme.typography.titleLarge)

                Spacer(modifier = Modifier.width(8.dp))

                Text(message, style = MaterialTheme.typography.bodyLarge)
            }//Column

        }//Row
    }//Column
}//BottomSheetContent