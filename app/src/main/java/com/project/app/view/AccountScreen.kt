@file:OptIn(ExperimentalMaterial3Api::class)

package com.project.app.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.project.app.model.User
import com.project.app.nav.Routes
import com.project.app.viewmodel.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    navHostController: NavHostController,
    userVM: UserViewModel,
    userId: Int
) {
    val allUsers: List<User> by userVM.allUsers.collectAsState(initial = emptyList())
    val user = remember(allUsers) { allUsers.find { it.userId == userId } }

    var name by remember(user) { mutableStateOf(user?.name ?: "") }
    var email by remember(user) { mutableStateOf(user?.email ?: "") }
    var phone by remember(user) { mutableStateOf(user?.phone ?: "") }
    var password by remember(user) { mutableStateOf(user?.password ?: "") }
    var role by remember(user) { mutableStateOf(user?.role ?: "") }

    val state by userVM.userData.collectAsState()
    var showConfirmDialog by remember { mutableStateOf(false) }

    var home by remember { mutableStateOf(state.homeAddress) }
    var work by remember { mutableStateOf(state.workAddress) }
    var school by remember { mutableStateOf(state.schoolAddress) }

    val operationStatus by userVM.operationStatus.collectAsState()

    // Create a single SnackbarHostState
    val snackbarHostState = remember { SnackbarHostState() }

    //set existing data to UI variables and show snackbar for status messages
    LaunchedEffect(state, operationStatus) {
        home = state.homeAddress
        work = state.workAddress
        school = state.schoolAddress

        operationStatus?.let { message ->
            snackbarHostState.showSnackbar(message)
            userVM.clearOperationStatus() // clear after showing
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("My Account") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
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
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                textStyle = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                textStyle = MaterialTheme.typography.bodyLarge,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone Number") },
                textStyle = MaterialTheme.typography.bodyLarge,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                textStyle = MaterialTheme.typography.bodyLarge,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text("Saved Addresses",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontSize = 24.sp,
                ))

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = home,
                onValueChange = { home = it},
                label = { Text("Home Address") },
                textStyle = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = work,
                onValueChange = { work = it },
                label = { Text("Work Address") },
                textStyle = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = school,
                onValueChange = { school = it },
                label = { Text("School Address") },
                textStyle = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (user != null) {
                        val updatedUser = user.copy(name = name, email = email, phone = phone, password = password, role = role)
                        userVM.updateUser(updatedUser)
                        //navHostController.popBackStack()
                    }
                    userVM.savePrefs(home, work, school)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary)
            ) {
                Text("Save Changes", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onPrimary)
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (showConfirmDialog) {
                AlertDialog(
                    onDismissRequest = { showConfirmDialog = false },
                    title = { Text("Delete Your Account") },
                    text = { Text("Are you sure you want to delete your account?") },
                    confirmButton = {
                        TextButton(onClick = {
                            //userVM.deleteUser(user!!)
                            if (user != null) {
                                userVM.deleteUser(user)
                                showConfirmDialog = false
                                navHostController.navigate(Routes.SignIn.routeName) {
                                    popUpTo(0)
                                }
                            }
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

            Button(
                onClick = {
                    showConfirmDialog = true
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                Text("Close Account", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}
