package com.arnasmat.dcrowd

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arnasmat.dcrowd.navigation.AppDestination
import com.arnasmat.dcrowd.navigation.NavDisplay
import com.arnasmat.dcrowd.navigation.rememberAppNavController
import com.arnasmat.dcrowd.ui.common.theme.DCrowdTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * MainActivity demonstrating Navigation 3
 *
 * Navigation 3 provides:
 * - Full control over the back stack (it's just a list!)
 * - Simple integration with Compose
 * - Support for adaptive layouts
 * - Type-safe navigation with parameters
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DCrowdTheme {
                DCrowdApp()
            }
        }
    }
}

@Composable
fun DCrowdApp() {
    // Create and remember the navigation controller
    // The back stack starts with the Home destination
    val navController = rememberAppNavController(
        initialDestination = AppDestination.Home
    )

    // Handle system back button
    BackHandler(enabled = navController.backStack.size > 1) {
        navController.popBackStack()
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        // NavDisplay is the core of Navigation 3
        // It displays the current destination from the back stack
        NavDisplay(
            navController = navController,
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        )
    }
}