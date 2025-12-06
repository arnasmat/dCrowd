package com.arnasmat.dcrowd.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Stable
class AppNavController(
    initialDestination: AppDestination
) {
    private val _backStack = mutableStateOf(listOf(initialDestination))
    val backStack: List<AppDestination> get() = _backStack.value

    val currentDestination: AppDestination?
        get() = backStack.lastOrNull()

    fun navigate(destination: AppDestination) {
        _backStack.value = _backStack.value + destination
    }

    fun popBackStack(): Boolean {
        if (_backStack.value.size <= 1) {
            return false
        }
        _backStack.value = _backStack.value.dropLast(1)
        return true
    }
    fun popBackTo(destination: AppDestination, inclusive: Boolean = false) {
        val index = _backStack.value.lastIndexOf(destination)
        if (index != -1) {
            val newIndex = if (inclusive) index else index + 1
            _backStack.value = _backStack.value.take(newIndex)
        }
    }

    fun replace(destination: AppDestination) {
        if (_backStack.value.isNotEmpty()) {
            _backStack.value = _backStack.value.dropLast(1) + destination
        } else {
            _backStack.value = listOf(destination)
        }
    }

    fun navigateRoot(destination: AppDestination) {
        _backStack.value = listOf(destination)
    }
}

@Composable
fun rememberAppNavController(
    initialDestination: AppDestination = AppDestination.ProjectList
): AppNavController {
    return remember { AppNavController(initialDestination) }
}

