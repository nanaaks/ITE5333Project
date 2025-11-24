package com.project.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.project.app.ui.theme.AppTheme
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.app.data.SettingsRepository
import com.project.app.nav.AppNavGraph
import com.project.app.viewmodel.SettingsViewModel
import com.project.app.viewmodel.SettingsViewModelFactory
import com.project.app.viewmodel.UserViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val settingsRepository = SettingsRepository(this)

        setContent {
            AppRoot(
                settingsRepository = settingsRepository
            )
        }
    }
}

@Composable
fun AppRoot(
    settingsRepository: SettingsRepository
) {
    val settingsVM: SettingsViewModel = viewModel(
        factory = SettingsViewModelFactory(settingsRepository)
    )

    val settingsState by settingsVM.settingsData.collectAsState()

    val userVM : UserViewModel = viewModel()

    AppTheme(darkTheme = settingsState.darkMode) {
        val navHostController = rememberNavController()

        Surface {
            AppNavGraph(
                navHostController,
                userVM,
                settingsVM
            )
        }
    }
}