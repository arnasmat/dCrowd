package com.arnasmat.dcrowd.navigation

import androidx.compose.runtime.Immutable
import java.math.BigInteger

/**
 * Navigation destinations for the crowdfunding app
 */
@Immutable
sealed interface AppDestination {
    @Immutable
    data object ProjectList : AppDestination

    @Immutable
    data object CreateProject : AppDestination

    @Immutable
    data class ProjectDetail(val projectIdx: BigInteger) : AppDestination

    @Immutable
    data object UserSelector : AppDestination

    @Immutable
    data object Web3Setup : AppDestination
}

