package com.arnasmat.dcrowd.ui.screens.projects

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arnasmat.dcrowd.data.web3.Project
import com.arnasmat.dcrowd.ui.common.composable.CoilImage
import org.web3j.utils.Convert
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectListScreen(
    onProjectClick: (BigInteger) -> Unit,
    onCreateProjectClick: () -> Unit,
    onSetupClick: () -> Unit,
    viewModel: ProjectListViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("dCrowd ar kazkas tokio") },
                actions = {
                    IconButton(onClick = onSetupClick) {
                        Icon(Icons.Default.Settings, "Setup")
                    }
                }
            )
        },
        floatingActionButton = {
            if (state.value.isConnected) {
                FloatingActionButton(onClick = onCreateProjectClick) {
                    Icon(Icons.Default.Add, "Create Project")
                }
            }
        }
    ) { padding ->
        PullToRefreshBox(
            isRefreshing = state.value.uiState is UiState.Loading,
            onRefresh = viewModel::loadProjects,
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    // terrible fix for this shit but idfc lmaoo
                    when (state.value.uiState) {
                        is UiState.Error -> { ErrorCard(errorMessage = state.value.uiState as UiState.Error)}
                        else -> {}
                    }
                }

                item {
                    if (!state.value.isConnected) {
                        NotConnectedCard()
                    } else if (state.value.projects.isEmpty() && state.value.uiState !is UiState.Loading) {
                        EmptyProjectCard()
                    }
                }

                if(state.value.projects.isNotEmpty()) {
                    items(
                        items = state.value.projects,
                        key = { it.index }
                    ) { projectWithIndex ->
                        ProjectCard(
                            project = projectWithIndex.project,
                            onClick = { onProjectClick(projectWithIndex.index) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ErrorCard(errorMessage: UiState.Error) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Text(
            text = errorMessage.message,
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.onErrorContainer
        )
    }
}

@Composable
fun NotConnectedCard() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Not Connected",
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = "Click settings to connect to contract",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun EmptyProjectCard() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "No active projects found",
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = "sukurk projekta plz :)",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun ProjectCard(
    project: Project,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Column {
            if (!project.headerImageUrl.isEmpty()) {
                CoilImage(
                    url = project.headerImageUrl,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(MaterialTheme.shapes.medium)
                )
            }
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
                        text = project.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    ProjectStatusCard(
                        isActive = project.isActive
                    )
                }


                Text(
                    text = project.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        FundedElement(project.totalFunded)
                    }

                    // Display current milestone info if available
                    if (project.currentMilestoneGoal != null && project.currentMilestoneDeadline != null) {
                        Column(horizontalAlignment = Alignment.End) {
                            MilestoneElement(
                                currentMilestoneGoal = project.currentMilestoneGoal,
                                currentMilestoneDeadline = project.currentMilestoneDeadline
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ProjectStatusCard(
    isActive: Boolean,
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Text(
            text = if (isActive) "Active" else "Inactive",
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = if (isActive) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onErrorContainer
        )
    }
}

@Composable
private fun FundedElement(
    totalFunded: BigInteger = BigInteger.ZERO
) {
    val oneEth = Convert.toWei("1", Convert.Unit.ETHER).toBigInteger()
    val oneGWei = Convert.toWei("1", Convert.Unit.GWEI).toBigInteger()
    val text = if (totalFunded >= oneEth) {
        weiToEthString(totalFunded) + "ETH"
    } else if (totalFunded >= oneGWei) {
        weiToGWeiString(totalFunded) + " GWei"
    } else {
        totalFunded.toString() + " Wei"
    }

    Text(
        text = "Funded",
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
private fun MilestoneElement(
    currentMilestoneGoal: BigInteger,
    currentMilestoneDeadline: BigInteger,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        MilestoneSingular(
            labelText = "Milestone goal",
            text = weiToEthString(currentMilestoneGoal) + " ETH"
        )
        MilestoneSingular(
            labelText = "Deadline",
            text = formatDeadlineTimestamp(currentMilestoneDeadline)
        )
    }

}

@Composable
private fun MilestoneSingular(
    labelText: String,
    text: String,
) {
    Column {
        Text(
            text = labelText,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

// TODO: move out of screen
fun weiToEthString(wei: BigInteger): String {
    val oneEth = Convert.toWei("1", Convert.Unit.ETHER)
    val eth = BigDecimal(wei).divide(oneEth, 4, RoundingMode.HALF_UP)
    return eth.stripTrailingZeros().toPlainString()
}

fun weiToGWeiString(wei: BigInteger): String {
    val oneGWei = Convert.toWei("1", Convert.Unit.GWEI)
    val gwei = BigDecimal(wei).divide(oneGWei, 4, RoundingMode.HALF_UP)
    return gwei.stripTrailingZeros().toPlainString()
}

fun formatDeadlineTimestamp(timestamp: BigInteger): String {
    return try {
        val dateFormat = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault())
        val date = java.util.Date(timestamp.toLong() * 1000) // Convert seconds to milliseconds
        dateFormat.format(date)
    } catch (e: Exception) {
        "N/A"
    }
}

@Composable
@Preview
private fun ProjectCardPreview() {
    ProjectCard(
        project = Project(
            name = "Jammi Kebabine",
            description = "As noriu ikurti pacia geriausia pacia kieciausia pacia visu megstamiausia kebabine prasau duokit man labai daug daug pinigu!",
            headerImageUrl = "https://picsum.photos/600/400",
            isActive = true,
            totalFunded = Convert.toWei("2.5", Convert.Unit.ETHER).toBigInteger(),
            owner = "0x1234567890abcdef1234567890abcdef12345678",
            currentMilestoneIdx = BigInteger.ONE,
        ),
        onClick = {}
    )
}

