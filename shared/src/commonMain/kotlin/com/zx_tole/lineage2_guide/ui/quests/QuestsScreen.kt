package com.zx_tole.lineage2_guide.ui.quests

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.zx_tole.lineage2_guide.domain.model.Quest
import com.zx_tole.lineage2_guide.ui.common.EmptyScreen
import com.zx_tole.lineage2_guide.ui.common.ErrorScreen
import com.zx_tole.lineage2_guide.ui.common.LoadingScreen
import com.zx_tole.lineage2_guide.ui.common.SyncIndicator
import com.zx_tole.lineage2_guide.ui.common.components.SearchBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestsScreen(
    viewModel: QuestsViewModel,
    onNavigateToDetail: (Long) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    when {
        uiState.isLoading -> {
            LoadingScreen(
                message = "Loading quests...",
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
            QuestsSuccessScreen(
                quests = uiState.quests,
                isRefreshing = uiState.isRefreshing,
                onRefresh = viewModel::refresh,
                onQuestClicked = onNavigateToDetail,
                searchQuery = uiState.searchQuery,
                onSearchQueryChanged = viewModel::onSearchQueryChanged
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuestsSuccessScreen(
    quests: List<Quest>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onQuestClicked: (Long) -> Unit,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Quests",
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

            if (quests.isEmpty()) {
                EmptyScreen(
                    message = if (searchQuery.isNotBlank()) "No quests match your search" else "No quests found",
                    icon = Icons.Default.List
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(quests, key = { it.id }) { quest ->
                        QuestCard(
                            quest = quest,
                            onClick = { onQuestClicked(quest.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun QuestCard(
    quest: Quest,
    onClick: () -> Unit
) {
    val (icon, iconColor) = getQuestIcon(quest.type)
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(iconColor.copy(alpha = 0.2f), MaterialTheme.shapes.medium),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = quest.name,
                    tint = iconColor,
                    modifier = Modifier.size(32.dp)
                )
            }
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = quest.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Type: ${quest.type}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = iconColor
                )
                Text(
                    text = "Start Level: ${quest.startLevel}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                quest.description?.let { desc ->
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = desc,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

/**
 * Get icon and color for quest based on type
 */
@Composable
private fun getQuestIcon(questType: com.zx_tole.lineage2_guide.domain.model.QuestType): Pair<androidx.compose.ui.graphics.vector.ImageVector, androidx.compose.ui.graphics.Color> {
    val lowerType = questType.name.lowercase()
    
    return when {
        // Main quests
        lowerType.contains("main") || lowerType.contains("primary") || lowerType.contains("story") ||
        lowerType.contains("episode") || lowerType.contains("chapter") ->
            Pair(Icons.Default.Stars, androidx.compose.ui.graphics.Color(0xFFFFDD44))
        
        // Side quests
        lowerType.contains("side") || lowerType.contains("extra") || lowerType.contains("bonus") ->
            Pair(Icons.Default.Person, androidx.compose.ui.graphics.Color(0xFF44BB44))
        
        // Daily quests
        lowerType.contains("daily") || lowerType.contains("day") || lowerType.contains("recurring") ->
            Pair(Icons.Default.Event, androidx.compose.ui.graphics.Color(0xFF4488FF))
        
        // Guild quests
        lowerType.contains("guild") || lowerType.contains("castle") || lowerType.contains("siege") ||
        lowerType.contains("faction") || lowerType.contains("party") ->
            Pair(Icons.Default.Group, androidx.compose.ui.graphics.Color(0xFFFF8844))
        
        // Event quests
        lowerType.contains("event") || lowerType.contains("festival") || lowerType.contains("special") ||
        lowerType.contains("limited") || lowerType.contains("holiday") ->
            Pair(Icons.Default.Celebration, androidx.compose.ui.graphics.Color(0xFFCC44FF))
        
        // Raid quests
        lowerType.contains("raid") || lowerType.contains("boss") || lowerType.contains("elite") ->
            Pair(Icons.Default.Dangerous, androidx.compose.ui.graphics.Color(0xFFFF4444))
        
        // Default
        else ->
            Pair(Icons.Default.List, androidx.compose.ui.graphics.Color(0xFF888888))
    }
}
