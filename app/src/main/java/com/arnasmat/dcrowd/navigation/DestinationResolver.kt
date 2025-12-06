package com.arnasmat.dcrowd.navigation

import androidx.compose.runtime.Composable
import com.arnasmat.dcrowd.ui.screens.detail.DetailScreen
import com.arnasmat.dcrowd.ui.screens.home.HomeScreen
import com.arnasmat.dcrowd.ui.screens.profile.ProfileScreen

/**
 * Resolves destination keys to composable content.
 * This is a key concept in Navigation 3 - you define how to render each destination.
 */
@Composable
fun ResolveDestination(
    destination: AppDestination,
    navController: AppNavController
) {
    when (destination) {
        is AppDestination.Home -> {
            HomeScreen(
                onNavigateToProfile = {
                    navController.navigate(AppDestination.Profile)
                },
                onNavigateToDetail = { itemId ->
                    navController.navigate(AppDestination.Detail(itemId))
                }
            )
        }

        is AppDestination.Profile -> {
            ProfileScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        is AppDestination.Detail -> {
            DetailScreen(
                itemId = destination.itemId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

