package com.arnasmat.dcrowd

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.arnasmat.dcrowd.navigation.CreateProject
import com.arnasmat.dcrowd.navigation.ProjectDetail
import com.arnasmat.dcrowd.navigation.ProjectList
import com.arnasmat.dcrowd.navigation.UserSelector
import com.arnasmat.dcrowd.navigation.Web3Setup
import com.arnasmat.dcrowd.ui.common.theme.DCrowdTheme
import com.arnasmat.dcrowd.ui.screens.create.CreateProjectScreen
import com.arnasmat.dcrowd.ui.screens.detail.ProjectDetailScreen
import com.arnasmat.dcrowd.ui.screens.projects.ProjectListScreen
import com.arnasmat.dcrowd.ui.screens.setup.Web3SetupScreen
import com.arnasmat.dcrowd.ui.screens.user.UserSelectorScreen
import dagger.hilt.android.AndroidEntryPoint

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
    val backStack = rememberNavBackStack(ProjectList)

    NavDisplay(
        backStack = backStack,
        // fuck this stupid fucking bug this caused me so much issues lmao. all it took was adding this
        entryDecorators = listOf(
            // Add the default decorators for managing scenes and saving state
            rememberSaveableStateHolderNavEntryDecorator(),
            // Then add the view model store decorator
            rememberViewModelStoreNavEntryDecorator()
        ),
        onBack = { backStack.removeLastOrNull() },
        entryProvider = { key ->
            when (key) {
                is ProjectList -> NavEntry(key) {
                    ProjectListScreen(
                        onProjectClick = { projectIdx ->
                            backStack.add(ProjectDetail(projectIdx))
                        },
                        onCreateProjectClick = {
                            backStack.add(CreateProject)
                        },
                        onUserSelectorClick = {
                            backStack.add(UserSelector)
                        },
                        onSetupClick = {
                            backStack.add(Web3Setup)
                        }
                    )
                }

                is CreateProject -> NavEntry(key) {
                    CreateProjectScreen(
                        onNavigateBack = {
                            backStack.removeLastOrNull()
                        },
                        onProjectCreated = { projectIdx ->
                            backStack.removeLastOrNull() // Remove CreateProject screen
                            backStack.add(ProjectDetail(projectIdx))
                        }
                    )
                }

                is ProjectDetail -> NavEntry(key) {
                    ProjectDetailScreen(
                        projectIdx = key.projectIdx,
                        onNavigateBack = {
                            backStack.removeLastOrNull()
                        }
                    )
                }

                is UserSelector -> NavEntry(key) {
                    UserSelectorScreen(
                        onNavigateBack = {
                            backStack.removeLastOrNull()
                        }
                    )
                }

                is Web3Setup -> NavEntry(key) {
                    Web3SetupScreen(
                        onNavigateBack = {
                            backStack.removeLastOrNull()
                        }
                    )
                }

                else -> NavEntry(key) {
                    Text("Unknown destination: $key")
                }
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}