package com.zx_tole.lineage2_guide.ui.quests

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.zx_tole.lineage2_guide.domain.model.QuestProgress
import com.zx_tole.lineage2_guide.ui.common.LoadingScreen
import com.zx_tole.lineage2_guide.ui.common.ErrorScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestDetailScreen(
    questId: Long,
    onNavigateBack: () -> Unit = {},
    viewModel: QuestDetailViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(questId) {
        viewModel.loadQuest(questId)
    }

    when {
        uiState.isLoading -> {
            LoadingScreen(message = "Loading quest...")
        }
        uiState.error != null -> {
            ErrorScreen(
                message = uiState.error!!,
                onRetry = { viewModel.loadQuest(questId) }
            )
        }
        uiState.quest != null -> {
            uiState.quest?.let { quest ->
                QuestDetailContent(quest = quest, onNavigateBack = onNavigateBack)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuestDetailContent(
    quest: com.zx_tole.lineage2_guide.domain.model.Quest,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quest", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = quest.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            // Type badge
            AssistChip(
                onClick = {},
                label = { Text(quest.type.name) }
            )

            // Progress
            quest.progressStatus?.let { status ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = when (status) {
                            QuestProgress.NOT_STARTED -> Icons.Default.Block
                            QuestProgress.IN_PROGRESS -> Icons.Default.Schedule
                            QuestProgress.COMPLETED -> Icons.Default.CheckCircle
                        },
                        contentDescription = null,
                        tint = when (status) {
                            QuestProgress.NOT_STARTED -> MaterialTheme.colorScheme.error
                            QuestProgress.IN_PROGRESS -> MaterialTheme.colorScheme.primary
                            QuestProgress.COMPLETED -> MaterialTheme.colorScheme.tertiary
                        },
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Status: $status",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // Start Level
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Start Level",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = quest.startLevel.toString(),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // NPC
            quest.npcName?.let { npcName ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "NPC",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = npcName,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Description
            quest.description?.let { desc ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = desc,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // Rewards
            if (quest.reward.isNotEmpty()) {
                Text(
                    text = "Rewards",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                quest.reward.forEach { reward ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = when (reward.type) {
                                    "EXP" -> Icons.Default.Bolt
                                    "SP" -> Icons.Default.Star
                                    "ITEM" -> Icons.Default.Inventory2
                                    "ADENA" -> Icons.Default.AttachMoney
                                    else -> Icons.Default.Star
                                },
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "${reward.type}: ${reward.value}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }

            // Prerequisites
            if (quest.prerequisites.isNotEmpty()) {
                Text(
                    text = "Prerequisites",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Card(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Requires ${quest.prerequisites.size} prerequisite quest(s)",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
