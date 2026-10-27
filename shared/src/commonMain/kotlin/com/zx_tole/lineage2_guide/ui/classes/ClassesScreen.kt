package com.zx_tole.lineage2_guide.ui.classes

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
import com.zx_tole.lineage2_guide.domain.model.GameClass
import com.zx_tole.lineage2_guide.ui.common.EmptyScreen
import com.zx_tole.lineage2_guide.ui.common.ErrorScreen
import com.zx_tole.lineage2_guide.ui.common.LoadingScreen
import com.zx_tole.lineage2_guide.ui.common.SyncIndicator
import com.zx_tole.lineage2_guide.ui.common.components.SearchBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassesScreen(
    viewModel: ClassesViewModel,
    onNavigateToDetail: (Long) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    when {
        uiState.isLoading -> {
            LoadingScreen(
                message = "Loading classes...",
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
            ClassesSuccessScreen(
                classes = uiState.classes,
                isRefreshing = uiState.isRefreshing,
                onRefresh = viewModel::refresh,
                onClassClicked = onNavigateToDetail,
                searchQuery = uiState.searchQuery,
                onSearchQueryChanged = viewModel::onSearchQueryChanged
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ClassesSuccessScreen(
    classes: List<GameClass>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onClassClicked: (Long) -> Unit,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Classes",
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

            if (classes.isEmpty()) {
                EmptyScreen(
                    message = if (searchQuery.isNotBlank()) "No classes match your search" else "No classes found",
                    icon = Icons.Default.Person
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(classes, key = { it.id }) { classData ->
                        ClassCard(
                            classData = classData,
                            onClick = { onClassClicked(classData.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ClassCard(
    classData: GameClass,
    onClick: () -> Unit
) {
    val lowerName = classData.name.lowercase()
    val tint = when {
        lowerName.contains("warrior") || lowerName.contains("knight") || lowerName.contains("adventurer") ->
            androidx.compose.ui.graphics.Color(0xFFEF4444)
        lowerName.contains("dark avenger") || lowerName.contains("sword master") ->
            androidx.compose.ui.graphics.Color(0xFFFF6600)
        lowerName.contains("warlord") || lowerName.contains("titans force") ->
            androidx.compose.ui.graphics.Color(0xFFCC0000)
        lowerName.contains("archer") || lowerName.contains("sylvain") || lowerName.contains("aetherpiers") ->
            androidx.compose.ui.graphics.Color(0xFF44BB44)
        lowerName.contains("mage") || lowerName.contains("wizard") || lowerName.contains("professor") || lowerName.contains("shillien templar") || lowerName.contains("ghost hunter") ->
            androidx.compose.ui.graphics.Color(0xFF4488FF)
        lowerName.contains("saint knight") || lowerName.contains("monk") || lowerName.contains("overlord") ->
            androidx.compose.ui.graphics.Color(0xFFFFDD44)
        lowerName.contains("oracle") || lowerName.contains("saint") ->
            androidx.compose.ui.graphics.Color(0xFFFFEE88)
        lowerName.contains("treasurer") || lowerName.contains("scout") || lowerName.contains("dusker") || lowerName.contains("phantom ranger") ->
            androidx.compose.ui.graphics.Color(0xFF88BB44)
        lowerName.contains("tyrant") || lowerName.contains("apostle") || lowerName.contains("saga") ->
            androidx.compose.ui.graphics.Color(0xFFAA00AA)
        lowerName.contains("warder") ->
            androidx.compose.ui.graphics.Color(0xFF6688CC)
        else ->
            androidx.compose.ui.graphics.Color(0xFF888888)
    }
    
    val icon = when {
        lowerName.contains("warrior") || lowerName.contains("knight") || lowerName.contains("adventurer") -> Icons.Default.Star
        lowerName.contains("dark avenger") || lowerName.contains("sword master") -> Icons.Default.Star
        lowerName.contains("warlord") || lowerName.contains("titans force") -> Icons.Default.Star
        lowerName.contains("archer") || lowerName.contains("sylvain") || lowerName.contains("aetherpiers") -> Icons.Default.SignalCellularAlt
        lowerName.contains("mage") || lowerName.contains("wizard") || lowerName.contains("professor") || lowerName.contains("shillien templar") || lowerName.contains("ghost hunter") -> Icons.Filled.SettingsBrightness
        lowerName.contains("saint knight") || lowerName.contains("monk") || lowerName.contains("overlord") -> Icons.Default.Favorite
        lowerName.contains("oracle") || lowerName.contains("saint") -> Icons.Default.Favorite
        lowerName.contains("treasurer") || lowerName.contains("scout") || lowerName.contains("dusker") || lowerName.contains("phantom ranger") -> Icons.Default.Visibility
        lowerName.contains("tyrant") || lowerName.contains("apostle") || lowerName.contains("saga") -> Icons.Default.AutoAwesome
        lowerName.contains("warder") -> Icons.Default.Shield
        else -> Icons.Default.Person
    }
    
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
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(tint.copy(alpha = 0.2f), MaterialTheme.shapes.medium),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = classData.name,
                    tint = tint,
                    modifier = Modifier.size(32.dp)
                )
            }
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = classData.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Race: ${classData.race}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                if (classData.subClasses.isNotEmpty()) {
                    Text(
                        text = "Sub-classes: ${classData.subClasses.joinToString(", ")}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                classData.description?.let { desc ->
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
