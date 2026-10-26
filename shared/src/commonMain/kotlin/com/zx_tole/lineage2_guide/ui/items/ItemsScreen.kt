package com.zx_tole.lineage2_guide.ui.items

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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.zx_tole.lineage2_guide.domain.model.*
import com.zx_tole.lineage2_guide.ui.common.EmptyScreen
import com.zx_tole.lineage2_guide.ui.common.ErrorScreen
import com.zx_tole.lineage2_guide.ui.common.LoadingScreen
import com.zx_tole.lineage2_guide.ui.common.SyncIndicator
import com.zx_tole.lineage2_guide.ui.common.components.RarityBadge
import com.zx_tole.lineage2_guide.ui.common.components.SearchBar
import com.zx_tole.lineage2_guide.ui.common.components.getRarityColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemsScreen(
    viewModel: ItemsViewModel,
    onNavigateToDetail: (Long) -> Unit = {},
    onNavigateToClassDetail: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var filteredItems by remember(uiState.items, searchQuery) {
        mutableStateOf(uiState.items)
    }

    // Filter items when searchQuery or items change
    LaunchedEffect(searchQuery, uiState.items) {
        if (searchQuery.isBlank()) {
            filteredItems = uiState.items
        } else {
            filteredItems = uiState.items.filter { item ->
                item.name.contains(searchQuery, ignoreCase = true) ||
                    item.description?.contains(searchQuery, ignoreCase = true) == true
            }
        }
    }

    when {
        uiState.isLoading -> {
            LoadingScreen(
                message = "Loading items...",
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
            ItemsSuccessScreen(
                items = filteredItems,
                isLoadingMore = uiState.isLoadingMore,
                hasMore = uiState.hasMore,
                isRefreshing = uiState.isRefreshing,
                searchQuery = searchQuery,
                onSearchQueryChanged = { searchQuery = it },
                onRefresh = viewModel::refresh,
                onLoadMore = viewModel::loadMore,
                onItemClicked = onNavigateToDetail,
                onClassClick = onNavigateToClassDetail
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ItemsSuccessScreen(
    items: List<Item>,
    isLoadingMore: Boolean,
    hasMore: Boolean,
    isRefreshing: Boolean,
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    onItemClicked: (Long) -> Unit,
    onClassClick: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Lineage 2",
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

            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items, key = { it.id }) { item ->
                    ItemCard(
                        item = item,
                        onClick = { onItemClicked(item.id) },
                        onClassClick = { onClassClick(item.classRestriction ?: "") }
                    )
                }

                if (items.isEmpty()) {
                    item {
                        EmptyScreen(
                            message = if (searchQuery.isNotBlank()) "No items match your search" else "No items found",
                            icon = Icons.Default.Inventory2
                        )
                    }
                }

                // Load More button
                if (hasMore && items.isNotEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Button(
                                onClick = onLoadMore,
                                enabled = !isLoadingMore,
                                modifier = Modifier.fillMaxWidth(0.6f)
                            ) {
                                if (isLoadingMore) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Text("Load More")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ItemCard(
    item: Item,
    onClick: () -> Unit,
    onClassClick: () -> Unit
) {
    val rarityColor = getRarityColor(item.rarity)

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
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(48.dp)
                    .background(rarityColor, MaterialTheme.shapes.small)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Display icon from server or fallback
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(rarityColor.copy(alpha = 0.2f), MaterialTheme.shapes.medium),
                contentAlignment = Alignment.Center
            ) {
                val iconUrl = item.iconUrl ?: "http://localhost:8080/api/v1/icons/items/${item.id}"
                AsyncImage(
                    model = iconUrl,
                    contentDescription = item.name,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = rarityColor,
                    fontWeight = FontWeight.SemiBold
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Lv. ${item.level}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "•",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = item.type.name,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                item.classRestriction?.let { restriction ->
                    Text(
                        text = restriction,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable(onClick = onClassClick)
                    )
                }
            }

            RarityBadge(rarity = item.rarity)
        }
    }
}
