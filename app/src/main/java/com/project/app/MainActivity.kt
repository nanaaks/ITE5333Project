package com.project.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.core.content.pm.ShortcutInfoCompat
import com.project.app.ui.theme.AppTheme
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.app.nav.NavGraph
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
    AppTheme {
        val navHostController = rememberNavController()
        val userViewModel: UserViewModel = viewModel()

        Surface {
            NavGraph(navController = navHostController, userViewModel = userViewModel)
        }
    }
}