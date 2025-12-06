package com.arnasmat.dcrowd.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier

@Composable
fun NavDisplay(
    navController: AppNavController,
    modifier: Modifier = Modifier
) {
    val currentDestination = navController.currentDestination

    Box(modifier = modifier.fillMaxSize()) {
        // Use AnimatedContent to animate between destinations
        AnimatedContent(
            targetState = currentDestination,
            transitionSpec = {
                // Slide in from right and fade in, while sliding out to left and fading out
                (slideInHorizontally { it } + fadeIn()).togetherWith(
                    slideOutHorizontally { -it } + fadeOut()
                )
            },
            label = "NavDisplay transition"
        ) { destination ->
            if (destination != null) {
                // Use key to ensure proper composition lifecycle
                key(destination) {
                    ResolveDestination(
                        destination = destination,
                        navController = navController
                    )
                }
            }
        }
    }
}

@Composable
fun NavDisplayWithFadeAnimation(
    navController: AppNavController,
    modifier: Modifier = Modifier
) {
    val currentDestination = navController.currentDestination

    Box(modifier = modifier.fillMaxSize()) {
        AnimatedContent(
            targetState = currentDestination,
            transitionSpec = {
                fadeIn().togetherWith(fadeOut())
            },
            label = "NavDisplay fade transition"
        ) { destination ->
            if (destination != null) {
                key(destination) {
                    ResolveDestination(
                        destination = destination,
                        navController = navController
                    )
                }
            }
        }
    }
}

