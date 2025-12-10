package com.arnasmat.dcrowd.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.math.RoundingMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                when (state.uiState) {
                    is SettingsUiState.Loading -> {
                        StatusCard(
                            message = "Processing...",
                            isLoading = true,
                            isError = false,
                            onDismiss = null
                        )
                    }
                    is SettingsUiState.Success -> {
                        StatusCard(
                            message = (state.uiState as SettingsUiState.Success).message,
                            isLoading = false,
                            isError = false,
                            onDismiss = { viewModel.clearMessage() }
                        )
                    }
                    is SettingsUiState.Error -> {
                        StatusCard(
                            message = (state.uiState as SettingsUiState.Error).message,
                            isLoading = false,
                            isError = true,
                            onDismiss = { viewModel.clearMessage() }
                        )
                    }
                    SettingsUiState.Initial -> {}
                }
            }

            item {
                Web3ConfigSection(
                    rpcUrl = state.rpcUrl,
                    contractAddress = state.contractAddress,
                    onRpcUrlChange = viewModel::updateRpcUrl,
                    onContractAddressChange = viewModel::updateContractAddress,
                    onSave = viewModel::saveConfiguration
                )
            }

            item {
                if (state.currentUser != null) {
                    CurrentUserCard(
                        user = state.currentUser,
                        balance = state.userBalance,
                        onLogout = viewModel::logoutUser,
                        onRefreshBalance = viewModel::refreshUserBalance
                    )
                } else {
                    LoginSection(
                        address = state.userAddress,
                        privateKey = state.userPrivateKey,
                        name = state.userName,
                        onAddressChange = viewModel::updateUserAddress,
                        onPrivateKeyChange = viewModel::updateUserPrivateKey,
                        onNameChange = viewModel::updateUserName,
                        onLogin = viewModel::loginUser
                    )
                }
            }
        }
    }
}

@Composable
fun CurrentUserCard(
    user: com.arnasmat.dcrowd.data.web3.UserCredentials?,
    balance: java.math.BigDecimal?,
    onLogout: () -> Unit,
    onRefreshBalance: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Current User",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )

                OutlinedButton(onClick = onLogout) {
                    Text("Logout")
                }
            }

            if (user != null) {
                if (user.name.isNotBlank()) {
                    Text(
                        text = user.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                }

                Text(
                    text = user.address,
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Balance: ${balance?.setScale(4, RoundingMode.HALF_UP) ?: "..."} ETH",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    OutlinedButton(
                        onClick = onRefreshBalance,
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text("Refresh", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }
}

@Composable
fun StatusCard(
    message: String,
    isLoading: Boolean,
    isError: Boolean,
    onDismiss: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when {
                isError -> MaterialTheme.colorScheme.errorContainer
                isLoading -> MaterialTheme.colorScheme.secondaryContainer
                else -> MaterialTheme.colorScheme.primaryContainer
            }
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            }

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f),
                color = when {
                    isError -> MaterialTheme.colorScheme.onErrorContainer
                    isLoading -> MaterialTheme.colorScheme.onSecondaryContainer
                    else -> MaterialTheme.colorScheme.onPrimaryContainer
                }
            )

            if (onDismiss != null && !isLoading) {
                IconButton(onClick = onDismiss) {
                    Text("✕")
                }
            }
        }
    }
}

@Composable
fun Web3ConfigSection(
    rpcUrl: String,
    contractAddress: String,
    onRpcUrlChange: (String) -> Unit,
    onContractAddressChange: (String) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Web3 Configuration",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            OutlinedTextField(
                value = rpcUrl,
                onValueChange = onRpcUrlChange,
                label = { Text("RPC URL") },
                placeholder = { Text("http://10.0.2.2:8545") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Uri,
                    imeAction = ImeAction.Next
                )
            )

            OutlinedTextField(
                value = contractAddress,
                onValueChange = onContractAddressChange,
                label = { Text("Contract Address") },
                placeholder = { Text("0x...") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                )
            )

            Button(
                onClick = onSave,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Configuration")
            }
        }
    }
}

@Composable
fun LoginSection(
    address: String,
    privateKey: String,
    name: String,
    onAddressChange: (String) -> Unit,
    onPrivateKeyChange: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    var privateKeyVisible by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Login",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "Enter your wallet credentials to login",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )

            OutlinedTextField(
                value = address,
                onValueChange = onAddressChange,
                label = { Text("Wallet Address") },
                placeholder = { Text("0x...") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )

            OutlinedTextField(
                value = privateKey,
                onValueChange = onPrivateKeyChange,
                label = { Text("Private Key") },
                placeholder = { Text("0x... or without 0x prefix") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (privateKeyVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                trailingIcon = {
                    IconButton(onClick = { privateKeyVisible = !privateKeyVisible }) {
                        Icon(
                            imageVector = if (privateKeyVisible) {
                                Icons.Filled.Visibility
                            } else {
                                Icons.Filled.VisibilityOff
                            },
                            contentDescription = if (privateKeyVisible) {
                                "Hide private key"
                            } else {
                                "Show private key"
                            }
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )

            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                label = { Text("Name (Optional)") },
                placeholder = { Text("My Wallet") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                )
            )

            Button(
                onClick = onLogin,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }
        }
    }
}

