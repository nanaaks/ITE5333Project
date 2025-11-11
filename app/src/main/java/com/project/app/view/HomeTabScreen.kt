package com.project.app.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.project.app.viewmodel.UserViewModel

@Composable
fun HomeTabScreen(
    navController: NavHostController,
    tabNavController: NavHostController,
    userVM: UserViewModel
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Welcome to the Home Tab!")
    }
}