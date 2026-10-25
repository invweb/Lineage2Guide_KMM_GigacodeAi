package com.zx_tole.lineage2_guide.ui.npcs

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
import com.zx_tole.lineage2_guide.domain.model.NpcType
import com.zx_tole.lineage2_guide.ui.common.LoadingScreen
import com.zx_tole.lineage2_guide.ui.common.ErrorScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NpcDetailScreen(
    npcId: Long,
    onNavigateBack: () -> Unit = {},
    viewModel: NpcDetailViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(npcId) {
        viewModel.loadNpc(npcId)
    }

    when {
        uiState.isLoading -> {
            LoadingScreen(message = "Loading NPC...")
        }
        uiState.error != null -> {
            ErrorScreen(
                message = uiState.error!!,
                onRetry = { viewModel.loadNpc(npcId) }
            )
        }
        uiState.npc != null -> {
            uiState.npc?.let { npc ->
                NpcDetailContent(npc = npc, onNavigateBack = onNavigateBack)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NpcDetailContent(
    npc: com.zx_tole.lineage2_guide.domain.model.Npc,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("NPC", fontWeight = FontWeight.Bold) },
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
                text = npc.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            // Type badge
            AssistChip(
                onClick = {},
                label = { Text(npc.type.name) },
                leadingIcon = {
                    Icon(
                        imageVector = when (npc.type) {
                            NpcType.QUEST_GIVER -> Icons.Default.List
                            NpcType.MERCHANT -> Icons.Default.ShoppingCart
                            NpcType.BOSS -> Icons.Default.Dangerous
                            NpcType.MONSTER -> Icons.Default.Pets
                            NpcType.TEACHER -> Icons.Default.School
                            NpcType.OTHER -> Icons.Default.Person
                        },
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            )

            // Location
            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Location",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = npc.location,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Description
            npc.description?.let { desc ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = desc,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // Related Quests
            if (npc.relatedQuestIds.isNotEmpty()) {
                Text(
                    text = "Related Quests",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Card(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "${npc.relatedQuestIds.size} quest(s) related to this NPC",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
