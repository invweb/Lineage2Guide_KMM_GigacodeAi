package com.zx_tole.lineage2_guide

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.zx_tole.lineage2_guide.common.Logger
import com.zx_tole.lineage2_guide.di.KoinHelper
import com.zx_tole.lineage2_guide.ui.items.ItemDetailViewModel
import com.zx_tole.lineage2_guide.ui.items.ItemsViewModel
import com.zx_tole.lineage2_guide.ui.classes.ClassDetailViewModel
import com.zx_tole.lineage2_guide.ui.classes.ClassesViewModel
import com.zx_tole.lineage2_guide.ui.npcs.NpcDetailViewModel
import com.zx_tole.lineage2_guide.ui.npcs.NpcsViewModel
import com.zx_tole.lineage2_guide.ui.navigation.AppNavigation
import com.zx_tole.lineage2_guide.ui.quests.QuestDetailViewModel
import com.zx_tole.lineage2_guide.ui.quests.QuestsViewModel
import com.zx_tole.lineage2_guide.ui.skills.SkillDetailViewModel
import com.zx_tole.lineage2_guide.ui.skills.SkillsViewModel
import com.zx_tole.lineage2_guide.ui.theme.Lineage2Theme
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

private const val DEFAULT_BASE_URL = "http://localhost:8080/api/v1"

object DesktopKoinManager : KoinComponent {
    val itemsViewModel: ItemsViewModel
        get() = get()
    val questsViewModel: QuestsViewModel
        get() = get()
    val skillsViewModel: SkillsViewModel
        get() = get()
    val classesViewModel: ClassesViewModel
        get() = get()
    val npcsViewModel: NpcsViewModel
        get() = get()
    val itemDetailViewModel: ItemDetailViewModel
        get() = get()
    val questDetailViewModel: QuestDetailViewModel
        get() = get()
    val skillDetailViewModel: SkillDetailViewModel
        get() = get()
    val classDetailViewModel: ClassDetailViewModel
        get() = get()
    val npcDetailViewModel: NpcDetailViewModel
        get() = get()

    fun init() {
        if (!KoinHelper.initialized) {
            KoinHelper.initKoin(DEFAULT_BASE_URL)
        }
    }
}

@Composable
fun DesktopAppContent() {
    Lineage2Theme {
        AppNavigation(
            itemsViewModel = DesktopKoinManager.itemsViewModel,
            questsViewModel = DesktopKoinManager.questsViewModel,
            skillsViewModel = DesktopKoinManager.skillsViewModel,
            classesViewModel = DesktopKoinManager.classesViewModel,
            npcsViewModel = DesktopKoinManager.npcsViewModel,
            itemDetailViewModel = DesktopKoinManager.itemDetailViewModel,
            questDetailViewModel = DesktopKoinManager.questDetailViewModel,
            skillDetailViewModel = DesktopKoinManager.skillDetailViewModel,
            classDetailViewModel = DesktopKoinManager.classDetailViewModel,
            npcDetailViewModel = DesktopKoinManager.npcDetailViewModel
        )
    }
}

fun main() {
    Logger.i("Starting Desktop application...")
    Logger.i("Base URL: $DEFAULT_BASE_URL")
    
    DesktopKoinManager.init()
    Logger.i("Koin initialized: ${KoinHelper.initialized}")
    
    try {
        val itemsVm = DesktopKoinManager.itemsViewModel
        Logger.i("ItemsViewModel loaded successfully")
    } catch (e: Exception) {
        Logger.e("Failed to load ItemsViewModel", e)
    }
    
    application {
        Window(
            title = "Lineage 2 Guide",
            onCloseRequest = ::exitApplication,
            state = WindowState(placement = WindowPlacement.Maximized)
        ) {
            DesktopAppContent()
        }
    }
    Logger.i("Application started")
}
