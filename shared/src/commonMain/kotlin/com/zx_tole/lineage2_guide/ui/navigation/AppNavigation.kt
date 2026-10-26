package com.zx_tole.lineage2_guide.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.zx_tole.lineage2_guide.ui.classes.ClassDetailScreen
import com.zx_tole.lineage2_guide.ui.classes.ClassesScreen
import com.zx_tole.lineage2_guide.ui.classes.ClassesViewModel
import com.zx_tole.lineage2_guide.ui.classes.ClassDetailViewModel
import com.zx_tole.lineage2_guide.ui.items.ItemDetailScreen
import com.zx_tole.lineage2_guide.ui.items.ItemsScreen
import com.zx_tole.lineage2_guide.ui.items.ItemsViewModel
import com.zx_tole.lineage2_guide.ui.items.ItemDetailViewModel
import com.zx_tole.lineage2_guide.ui.npcs.NpcDetailScreen
import com.zx_tole.lineage2_guide.ui.npcs.NpcsScreen
import com.zx_tole.lineage2_guide.ui.npcs.NpcsViewModel
import com.zx_tole.lineage2_guide.ui.npcs.NpcDetailViewModel
import com.zx_tole.lineage2_guide.ui.quests.QuestDetailScreen
import com.zx_tole.lineage2_guide.ui.quests.QuestsScreen
import com.zx_tole.lineage2_guide.ui.quests.QuestsViewModel
import com.zx_tole.lineage2_guide.ui.quests.QuestDetailViewModel
import com.zx_tole.lineage2_guide.ui.skills.SkillDetailScreen
import com.zx_tole.lineage2_guide.ui.skills.SkillsScreen
import com.zx_tole.lineage2_guide.ui.skills.SkillsViewModel
import com.zx_tole.lineage2_guide.ui.skills.SkillDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
    itemsViewModel: ItemsViewModel,
    questsViewModel: QuestsViewModel,
    skillsViewModel: SkillsViewModel,
    classesViewModel: ClassesViewModel,
    npcsViewModel: NpcsViewModel,
    itemDetailViewModel: ItemDetailViewModel,
    questDetailViewModel: QuestDetailViewModel,
    skillDetailViewModel: SkillDetailViewModel,
    classDetailViewModel: ClassDetailViewModel,
    npcDetailViewModel: NpcDetailViewModel
) {
    // All ViewModels already have SettingsManager injected via Koin {
    var currentTab by remember { mutableIntStateOf(0) }
    var selectedScreenId by remember { mutableLongStateOf(0L) }
    var showDetail by remember { mutableStateOf(false) }

    val tabs = listOf(
        TabDef(Screen.Items, Icons.Default.Inventory2),
        TabDef(Screen.Quests, Icons.Default.List),
        TabDef(Screen.Skills, Icons.Default.Bolt),
        TabDef(Screen.Classes, Icons.Default.Person),
        TabDef(Screen.Npcs, Icons.Default.LocationOn)
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { index, tab ->
                    NavigationBarItem(
                        icon = { Icon(tab.icon, contentDescription = tab.screen.title) },
                        label = { Text(tab.screen.title) },
                        selected = currentTab == index,
                        onClick = {
                            currentTab = index
                            showDetail = false
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            when (currentTab) {
                0 -> if (!showDetail) {
                    ItemsScreen(
                        viewModel = itemsViewModel,
                        onNavigateToDetail = { id ->
                            selectedScreenId = id
                            showDetail = true
                        }
                    )
                } else {
                    ItemDetailScreen(
                        itemId = selectedScreenId,
                        onNavigateBack = { showDetail = false },
                        viewModel = itemDetailViewModel
                    )
                }

                1 -> if (!showDetail) {
                    QuestsScreen(
                        viewModel = questsViewModel,
                        onNavigateToDetail = { id ->
                            selectedScreenId = id
                            showDetail = true
                        }
                    )
                } else {
                    QuestDetailScreen(
                        questId = selectedScreenId,
                        onNavigateBack = { showDetail = false },
                        viewModel = questDetailViewModel
                    )
                }

                2 -> if (!showDetail) {
                    SkillsScreen(
                        viewModel = skillsViewModel,
                        onNavigateToDetail = { id ->
                            selectedScreenId = id
                            showDetail = true
                        }
                    )
                } else {
                    SkillDetailScreen(
                        skillId = selectedScreenId,
                        onNavigateBack = { showDetail = false },
                        viewModel = skillDetailViewModel
                    )
                }

                3 -> if (!showDetail) {
                    ClassesScreen(
                        viewModel = classesViewModel,
                        onNavigateToDetail = { id ->
                            selectedScreenId = id
                            showDetail = true
                        }
                    )
                } else {
                    ClassDetailScreen(
                        classId = selectedScreenId,
                        onNavigateBack = { showDetail = false },
                        viewModel = classDetailViewModel
                    )
                }

                4 -> if (!showDetail) {
                    NpcsScreen(
                        viewModel = npcsViewModel,
                        onNavigateToDetail = { id ->
                            selectedScreenId = id
                            showDetail = true
                        }
                    )
                } else {
                    NpcDetailScreen(
                        npcId = selectedScreenId,
                        onNavigateBack = { showDetail = false },
                        viewModel = npcDetailViewModel
                    )
                }
            }
        }
    }
}

private data class TabDef(val screen: Screen, val icon: ImageVector)
