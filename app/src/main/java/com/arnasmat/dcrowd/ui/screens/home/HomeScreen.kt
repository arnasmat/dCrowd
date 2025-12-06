package com.arnasmat.dcrowd.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Home screen that demonstrates Navigation 3 navigation
 */
@Composable
fun HomeScreen(
    onNavigateToProfile: () -> Unit,
    onNavigateToDetail: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "🎉 Navigation 3",
            style = MaterialTheme.typography.displaySmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Home Screen",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Navigation 3 provides:\n• Full back stack control\n• Flexible navigation\n• Adaptive layouts\n• Simple Compose integration",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = onNavigateToProfile) {
            Text("Navigate to Profile")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = { onNavigateToDetail("item-123") }) {
            Text("View Detail: Item 123")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = { onNavigateToDetail("item-456") }) {
            Text("View Detail: Item 456")
        }
    }
}

