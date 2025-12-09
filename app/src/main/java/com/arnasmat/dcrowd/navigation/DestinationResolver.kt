package com.arnasmat.dcrowd.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arnasmat.dcrowd.ui.screens.create.CreateProjectScreen
import com.arnasmat.dcrowd.ui.screens.detail.ProjectDetailScreen
import com.arnasmat.dcrowd.ui.screens.projects.ProjectListScreen
import com.arnasmat.dcrowd.ui.screens.setup.Web3SetupScreen
import com.arnasmat.dcrowd.ui.screens.user.UserSelectorScreen

@Composable
fun ResolveDestination(
    destination: AppDestination,
    navController: AppNavController
) {
    when (destination) {
        is AppDestination.ProjectList -> {
            ProjectListScreen(
                onProjectClick = { projectIdx ->
                    navController.navigate(AppDestination.ProjectDetail(projectIdx))
                },
                onCreateProjectClick = {
                    navController.navigate(AppDestination.CreateProject)
                },
                onUserSelectorClick = {
                    navController.navigate(AppDestination.UserSelector)
                },
                onSetupClick = {
                    navController.navigate(AppDestination.Web3Setup)
                }
            )
        }

        is AppDestination.CreateProject -> {
            CreateProjectScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onProjectCreated = { projectIdx ->
                    navController.popBackStack()
                    navController.navigate(AppDestination.ProjectDetail(projectIdx))
                }
            )
        }

        is AppDestination.ProjectDetail -> {
            ProjectDetailScreen(
                projectIdx = destination.projectIdx,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        is AppDestination.UserSelector -> {
            UserSelectorScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        is AppDestination.Web3Setup -> {
            Web3SetupScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}

