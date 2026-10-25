package com.zx_tole.lineage2_guide.ui.classes

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
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
            Text(
                text = classData.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Race: ${classData.race}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            if (classData.subClasses.isNotEmpty()) {
                Text(
                    text = "Sub-classes: ${classData.subClasses.joinToString(", ")}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            classData.description?.let { desc ->
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
