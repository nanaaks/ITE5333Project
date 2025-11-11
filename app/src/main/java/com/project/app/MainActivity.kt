package com.project.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.pm.ShortcutInfoCompat
import com.project.app.ui.theme.AppTheme
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.app.nav.AppNavGraph
import com.project.app.viewmodel.UserViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppRoot()
        }
    }
}

@Composable
fun AppRoot() {

    val userVM : UserViewModel = viewModel()

    var isDarkMode by remember { mutableStateOf(false) }

    AppTheme {
        val navHostController = rememberNavController()

        Surface {
            AppNavGraph(
                navHostController,
                userVM,
                toggleColorScheme = {
                    isDarkMode = !isDarkMode
                }
            )
        }
    }
}