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

/**
 * NavDisplay is the core component of Navigation 3.
 * It displays the current destination from the back stack and handles transitions.
 *
 * In Navigation 3, you have full control over how destinations are displayed,
 * including support for adaptive layouts that can show multiple destinations at once.
 */
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

/**
 * Alternative NavDisplay with different animation strategy.
 * This demonstrates how Navigation 3 gives you full control over animations.
 */
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

