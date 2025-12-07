package com.project.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.project.app.data.AppDatabase
import com.project.app.data.RideRepository
import com.project.app.data.SettingsRepository
import com.project.app.data.UserRepository
import com.project.app.ui.theme.AppTheme
import com.project.app.nav.AppNavGraph
import com.project.app.viewmodel.RideViewModel
import com.project.app.viewmodel.RideViewModelFactory
import com.project.app.viewmodel.SettingsViewModel
import com.project.app.viewmodel.SettingsViewModelFactory
import com.project.app.viewmodel.UserViewModel
import com.project.app.viewmodel.UserViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db : AppDatabase by lazy {
            val scope = CoroutineScope(SupervisorJob())
            AppDatabase.getDatabase(this, scope)
        }

        // Pass DAO into UserRepository
        val userRepository = UserRepository(
            context = this,
            userDao = db.userDao()
        )

        val settingsRepository = SettingsRepository(this)

        val rideRepository = RideRepository(db.rideDao())

        setContent {
            AppRoot(
                settingsRepository = settingsRepository,
                userRepository = userRepository,
                rideRepository = rideRepository
            )
        }
    }
}
@Composable
fun AppRoot(
    settingsRepository: SettingsRepository,
    userRepository: UserRepository,
    rideRepository: RideRepository
) {
    val userVM: UserViewModel = viewModel(
        factory = UserViewModelFactory(userRepository)
    )

    val settingsVM: SettingsViewModel = viewModel(
        factory = SettingsViewModelFactory(settingsRepository)
    )

    val settingsState by settingsVM.settingsData.collectAsState()

    val rideVM : RideViewModel = viewModel(
        factory = RideViewModelFactory(rideRepository)
    )

    AppTheme(darkTheme = settingsState.darkMode) {
        val navHostController = rememberNavController()

        Surface {
            AppNavGraph(
                navHostController,
                userVM,
                settingsVM,
                rideVM
            )
        }
    }
}
