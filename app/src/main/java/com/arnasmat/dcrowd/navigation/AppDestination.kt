package com.arnasmat.dcrowd.navigation

import androidx.compose.runtime.Immutable

/**
 * Keys that uniquely identify destinations in the app.
 * In Navigation 3, keys are used to identify content in the back stack.
 */
@Immutable
sealed interface AppDestination {
    @Immutable
    data object Home : AppDestination

    @Immutable
    data object Profile : AppDestination

    @Immutable
    data class Detail(val itemId: String) : AppDestination
}

