package com.zx_tole.lineage2_guide.di

import com.zx_tole.lineage2_guide.BuildConfig
import com.zx_tole.lineage2_guide.di.DefaultSettingsManager
import com.zx_tole.lineage2_guide.data.settings.SettingsManager
import com.zx_tole.lineage2_guide.ui.items.ItemsViewModel
import com.zx_tole.lineage2_guide.ui.items.ItemDetailViewModel
import com.zx_tole.lineage2_guide.ui.quests.QuestsViewModel
import com.zx_tole.lineage2_guide.ui.quests.QuestDetailViewModel
import com.zx_tole.lineage2_guide.ui.skills.SkillsViewModel
import com.zx_tole.lineage2_guide.ui.skills.SkillDetailViewModel
import com.zx_tole.lineage2_guide.ui.classes.ClassesViewModel
import com.zx_tole.lineage2_guide.ui.classes.ClassDetailViewModel
import com.zx_tole.lineage2_guide.ui.npcs.NpcsViewModel
import com.zx_tole.lineage2_guide.ui.npcs.NpcDetailViewModel
import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

fun appModule(): List<Module> = listOf(
    viewModelModule()
)

fun viewModelModule(): Module = module {
    viewModel { ItemDetailViewModel(get()) }
    viewModel { ItemsViewModel(get()) }
    viewModel { QuestsViewModel(get()) }
    viewModel { QuestDetailViewModel(get()) }
    viewModel { SkillsViewModel(get()) }
    viewModel { SkillDetailViewModel(get()) }
    viewModel { ClassesViewModel(get()) }
    viewModel { ClassDetailViewModel(get()) }
    viewModel { NpcsViewModel(get()) }
    viewModel { NpcDetailViewModel(get()) }
}

fun initKoin(context: Context, settingsManager: SettingsManager = DefaultSettingsManager()) {
    startKoin {
        androidContext(context)
        modules(sharedModule(settingsManager) + appModule())
    }
}
