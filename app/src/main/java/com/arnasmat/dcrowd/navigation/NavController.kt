package com.arnasmat.dcrowd.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

/**
 * Navigation 3 controller that manages the back stack.
 * The back stack is simply a list of destination keys that users have navigated to.
 *
 * This is the core principle of Navigation 3: the back stack is just a list!
 */
@Stable
class AppNavController(
    initialDestination: AppDestination
) {
    // The back stack is just a mutable list of destinations
    private val _backStack = mutableStateOf(listOf(initialDestination))
    val backStack: List<AppDestination> get() = _backStack.value

    // Current destination is the last item in the back stack
    val currentDestination: AppDestination?
        get() = backStack.lastOrNull()

    /**
     * Navigate to a new destination by adding it to the back stack
     */
    fun navigate(destination: AppDestination) {
        _backStack.value = _backStack.value + destination
    }

    /**
     * Pop the back stack to go back
     */
    fun popBackStack(): Boolean {
        if (_backStack.value.size <= 1) {
            return false
        }
        _backStack.value = _backStack.value.dropLast(1)
        return true
    }

    /**
     * Navigate back to a specific destination, removing all destinations after it
     */
    fun popBackTo(destination: AppDestination, inclusive: Boolean = false) {
        val index = _backStack.value.lastIndexOf(destination)
        if (index != -1) {
            val newIndex = if (inclusive) index else index + 1
            _backStack.value = _backStack.value.take(newIndex)
        }
    }

    /**
     * Replace the current destination with a new one
     */
    fun replace(destination: AppDestination) {
        if (_backStack.value.isNotEmpty()) {
            _backStack.value = _backStack.value.dropLast(1) + destination
        } else {
            _backStack.value = listOf(destination)
        }
    }

    /**
     * Clear the back stack and navigate to a new destination
     */
    fun navigateRoot(destination: AppDestination) {
        _backStack.value = listOf(destination)
    }
}

/**
 * Remember an AppNavController across recompositions
 */
@Composable
fun rememberAppNavController(
    initialDestination: AppDestination = AppDestination.Home
): AppNavController {
    return remember { AppNavController(initialDestination) }
}

