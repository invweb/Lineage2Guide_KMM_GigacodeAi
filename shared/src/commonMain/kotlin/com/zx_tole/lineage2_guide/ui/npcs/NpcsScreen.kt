package com.zx_tole.lineage2_guide.ui.npcs

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material3.Icon
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.zx_tole.lineage2_guide.domain.model.Npc
import com.zx_tole.lineage2_guide.domain.model.NpcType
import com.zx_tole.lineage2_guide.ui.common.EmptyScreen
import com.zx_tole.lineage2_guide.ui.common.ErrorScreen
import com.zx_tole.lineage2_guide.ui.common.LoadingScreen
import com.zx_tole.lineage2_guide.ui.common.SyncIndicator
import com.zx_tole.lineage2_guide.ui.common.components.SearchBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NpcsScreen(
    viewModel: NpcsViewModel,
    onNavigateToDetail: (Long) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    when {
        uiState.isLoading -> {
            LoadingScreen(
                message = "Loading NPCs...",
                modifier = Modifier.fillMaxSize()
            )
        }
        uiState.error != null -> {
            ErrorScreen(
                message = uiState.error!!,
                onRetry = viewModel::refresh,
                modifier = Modifier.fillMaxSize()
            )
        }
        else -> {
            NpcsSuccessScreen(
                npcs = uiState.npcs,
                isRefreshing = uiState.isRefreshing,
                onRefresh = viewModel::refresh,
                onNpcClicked = onNavigateToDetail,
                searchQuery = uiState.searchQuery,
                onSearchQueryChanged = viewModel::onSearchQueryChanged
            )
        }
    }
}

/**
 * Get color for NPC type
 */
@Composable
private fun npcTypeColor(type: NpcType): androidx.compose.ui.graphics.Color {
    return when (type) {
        NpcType.QUEST_GIVER -> androidx.compose.ui.graphics.Color(0xFF44BB44) // Green
        NpcType.MERCHANT -> androidx.compose.ui.graphics.Color(0xFFFFDD44) // Yellow
        NpcType.BOSS -> androidx.compose.ui.graphics.Color(0xFFFF4444) // Red
        NpcType.MONSTER -> androidx.compose.ui.graphics.Color(0xFF888888) // Gray
        NpcType.TEACHER -> androidx.compose.ui.graphics.Color(0xFF4488FF) // Blue
        NpcType.OTHER -> androidx.compose.ui.graphics.Color(0xFF888888) // Gray
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NpcsSuccessScreen(
    npcs: List<Npc>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onNpcClicked: (Long) -> Unit,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "NPCs",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                actions = {
                    SyncIndicator(isSyncing = isRefreshing)
                    IconButton(onClick = onRefresh) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            SearchBar(
                query = searchQuery,
                onQueryChanged = onSearchQueryChanged,
                onClear = { onSearchQueryChanged("") }
            )

            if (npcs.isEmpty()) {
                EmptyScreen(
                    message = if (searchQuery.isNotBlank()) "No NPCs match your search" else "No NPCs found",
                    icon = Icons.Default.LocationOn
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(npcs, key = { it.id }) { npc ->
                        NpcCard(
                            npc = npc,
                            onClick = { onNpcClicked(npc.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NpcCard(
    npc: Npc,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Display NPC icon based on type with colors
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(npcTypeColor(npc.type).copy(alpha = 0.2f), MaterialTheme.shapes.medium),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (npc.type) {
                            NpcType.QUEST_GIVER -> Icons.Default.Mail
                            NpcType.MERCHANT -> Icons.Default.ShoppingCart
                            NpcType.BOSS -> Icons.Default.Dangerous
                            NpcType.MONSTER -> Icons.Default.Security
                            NpcType.TEACHER -> Icons.Default.School
                            NpcType.OTHER -> Icons.Default.Person
                        },
                        contentDescription = null,
                        tint = npcTypeColor(npc.type),
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = npc.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Type: ${npc.type}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Location: ${npc.location}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            npc.description?.let { desc ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
