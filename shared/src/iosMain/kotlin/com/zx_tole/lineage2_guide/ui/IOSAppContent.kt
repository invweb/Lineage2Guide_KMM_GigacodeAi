package com.zx_tole.lineage2_guide.ui

import androidx.compose.runtime.Composable
import com.zx_tole.lineage2_guide.data.settings.IOSSettingsManager
import com.zx_tole.lineage2_guide.di.sharedModule
import com.zx_tole.lineage2_guide.ui.classes.ClassDetailViewModel
import com.zx_tole.lineage2_guide.ui.classes.ClassesViewModel
import com.zx_tole.lineage2_guide.ui.items.ItemDetailViewModel
import com.zx_tole.lineage2_guide.ui.items.ItemsViewModel
import com.zx_tole.lineage2_guide.ui.npcs.NpcDetailViewModel
import com.zx_tole.lineage2_guide.ui.npcs.NpcsViewModel
import com.zx_tole.lineage2_guide.ui.navigation.AppNavigation
import com.zx_tole.lineage2_guide.ui.quests.QuestDetailViewModel
import com.zx_tole.lineage2_guide.ui.quests.QuestsViewModel
import com.zx_tole.lineage2_guide.ui.skills.SkillDetailViewModel
import com.zx_tole.lineage2_guide.ui.skills.SkillsViewModel
import com.zx_tole.lineage2_guide.ui.theme.Lineage2Theme
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.component.get
import platform.UIKit.UIViewController

object IOSKoinManager : KoinComponent {
    var initialized = false
        private set

    val itemsViewModel: ItemsViewModel by lazy { get() }
    val questsViewModel: QuestsViewModel by lazy { get() }
    val skillsViewModel: SkillsViewModel by lazy { get() }
    val classesViewModel: ClassesViewModel by lazy { get() }
    val npcsViewModel: NpcsViewModel by lazy { get() }
    val itemDetailViewModel: ItemDetailViewModel by lazy { get() }
    val questDetailViewModel: QuestDetailViewModel by lazy { get() }
    val skillDetailViewModel: SkillDetailViewModel by lazy { get() }
    val classDetailViewModel: ClassDetailViewModel by lazy { get() }
    val npcDetailViewModel: NpcDetailViewModel by lazy { get() }

    fun init() {
        if (initialized) return
        stopKoin()
        val settingsManager = IOSSettingsManager()
        startKoin {
            modules(sharedModule(settingsManager))
        }
        initialized = true
    }
}

@OptIn(ExperimentalForeignApi::class)
@Suppress("unused")
fun IOSAppContentBridge(): UIViewController {
    IOSKoinManager.init()

    return androidx.compose.ui.window.ComposeUIViewController(configure = { }) {
        IOSAppContent()
    }
}

@Composable
fun IOSAppContent() {
    Lineage2Theme {
        AppNavigation(
            itemsViewModel = IOSKoinManager.itemsViewModel,
            questsViewModel = IOSKoinManager.questsViewModel,
            skillsViewModel = IOSKoinManager.skillsViewModel,
            classesViewModel = IOSKoinManager.classesViewModel,
            npcsViewModel = IOSKoinManager.npcsViewModel,
            itemDetailViewModel = IOSKoinManager.itemDetailViewModel,
            questDetailViewModel = IOSKoinManager.questDetailViewModel,
            skillDetailViewModel = IOSKoinManager.skillDetailViewModel,
            classDetailViewModel = IOSKoinManager.classDetailViewModel,
            npcDetailViewModel = IOSKoinManager.npcDetailViewModel
        )
    }
}
