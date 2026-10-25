package com.zx_tole.lineage2_guide.ui.skills

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
import com.zx_tole.lineage2_guide.domain.model.SkillType
import com.zx_tole.lineage2_guide.ui.common.LoadingScreen
import com.zx_tole.lineage2_guide.ui.common.ErrorScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkillDetailScreen(
    skillId: Long,
    onNavigateBack: () -> Unit = {},
    viewModel: SkillDetailViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(skillId) {
        viewModel.loadSkill(skillId)
    }

    when {
        uiState.isLoading -> {
            LoadingScreen(message = "Loading skill...")
        }
        uiState.error != null -> {
            ErrorScreen(
                message = uiState.error!!,
                onRetry = { viewModel.loadSkill(skillId) }
            )
        }
        uiState.skill != null -> {
            SkillDetailContent(skill = uiState.skill, onNavigateBack = onNavigateBack)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SkillDetailContent(
    skill: com.zx_tole.lineage2_guide.domain.model.Skill?,
    onNavigateBack: () -> Unit
) {
    skill ?: return
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Skill", fontWeight = FontWeight.Bold) },
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
                text = skill.name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            // Type badge
            AssistChip(
                onClick = {},
                label = { Text(skill.type.name) },
                leadingIcon = {
                    Icon(
                        imageVector = if (skill.type == SkillType.ACTIVE) Icons.Default.Bolt else Icons.Default.AutoFixHigh,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            )

            // Stats grid
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Details", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))

                    StatRow("Class", skill.classRestriction)
                    StatRow("Level", skill.level.toString())

                    skill.manaCost?.let { cost ->
                        StatRow("Mana Cost", "$cost")
                    }

                    skill.cooldown?.let { cd ->
                        StatRow("Cooldown", "$cd sec")
                    }

                    skill.range?.let { range ->
                        StatRow("Range", range)
                    }
                }
            }

            // Description
            skill.description?.let { desc ->
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
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
