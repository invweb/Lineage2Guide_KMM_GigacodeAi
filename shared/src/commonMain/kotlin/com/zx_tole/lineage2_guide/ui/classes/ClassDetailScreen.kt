package com.zx_tole.lineage2_guide.ui.classes

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
import com.zx_tole.lineage2_guide.ui.common.LoadingScreen
import com.zx_tole.lineage2_guide.ui.common.ErrorScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassDetailScreen(
    classId: Long,
    onNavigateBack: () -> Unit = {},
    viewModel: ClassDetailViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(classId) {
        viewModel.loadClass(classId)
    }

    when {
        uiState.isLoading -> {
            LoadingScreen(message = "Loading class...")
        }
        uiState.error != null -> {
            ErrorScreen(
                message = uiState.error!!,
                onRetry = { viewModel.loadClass(classId) }
            )
        }
        uiState.classData != null -> {
            uiState.classData?.let { classData ->
                ClassDetailContent(classData = classData, onNavigateBack = onNavigateBack)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ClassDetailContent(
    classData: com.zx_tole.lineage2_guide.domain.model.GameClass,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Class", fontWeight = FontWeight.Bold) },
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
                text = classData.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            // Race
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
                            text = "Race",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = classData.race,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Sub-classes
            if (classData.subClasses.isNotEmpty()) {
                Text(
                    text = "Sub-classes",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                classData.subClasses.forEach { subClass ->
                    AssistChip(
                        onClick = {},
                        label = { Text(subClass) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            // Base Stats
            if (classData.baseStats.isNotEmpty()) {
                Text(
                    text = "Base Stats",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        classData.baseStats.forEach { (stat, value) ->
                            StatRow(stat, value.toString())
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                }
            }

            // Description
            classData.description?.let { desc ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = desc,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun StatRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}
