package com.zx_tole.lineage2_guide.di

import com.zx_tole.lineage2_guide.data.settings.SettingsManager
import com.zx_tole.lineage2_guide.data.mapper.*
import com.zx_tole.lineage2_guide.data.network.Lineage2ApiService
import com.zx_tole.lineage2_guide.data.network.createHttpClient
import com.zx_tole.lineage2_guide.data.repository.*
import com.zx_tole.lineage2_guide.domain.repository.*
import com.zx_tole.lineage2_guide.domain.usecase.*
import com.zx_tole.lineage2_guide.ui.classes.ClassDetailViewModel
import com.zx_tole.lineage2_guide.ui.classes.ClassesViewModel
import com.zx_tole.lineage2_guide.ui.items.ItemDetailViewModel
import com.zx_tole.lineage2_guide.ui.items.ItemsViewModel
import com.zx_tole.lineage2_guide.ui.npcs.NpcDetailViewModel
import com.zx_tole.lineage2_guide.ui.npcs.NpcsViewModel
import com.zx_tole.lineage2_guide.ui.quests.QuestDetailViewModel
import com.zx_tole.lineage2_guide.ui.quests.QuestsViewModel
import com.zx_tole.lineage2_guide.ui.settings.SettingsViewModel
import com.zx_tole.lineage2_guide.ui.skills.SkillDetailViewModel
import com.zx_tole.lineage2_guide.ui.skills.SkillsViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

fun settingsModule(settingsManager: SettingsManager): Module = module {
    single { settingsManager }
}

fun sharedModule(settingsManager: SettingsManager): List<Module> = listOf(
    settingsModule(settingsManager),
    networkModule(settingsManager),
    mapperModule(),
    repositoryModule(),
    viewModelModule(),
    useCaseModule()
)

fun networkModule(settingsManager: SettingsManager): Module = module {
    single { createHttpClient(settingsManager.baseUrl) }
    single { Lineage2ApiService(get(), settingsManager.baseUrl) }
}

fun mapperModule(): Module = module {
    single { ItemMapper() }
    single { QuestMapper() }
    single { SkillMapper() }
    single { ClassMapper() }
    single { NpcMapper() }
}

fun repositoryModule(): Module = module {
    single<ItemsRepository> { ItemsRepositoryImpl(get(), get()) }
    single<QuestsRepository> { QuestsRepositoryImpl(get(), get()) }
    single<SkillsRepository> { SkillsRepositoryImpl(get(), get()) }
    single<ClassesRepository> { ClassesRepositoryImpl(get(), get()) }
    single<NpcsRepository> { NpcsRepositoryImpl(get(), get()) }
}

fun useCaseModule(): Module = module {
    single { GetItemsUseCase(get()) }
    single { GetTotalCountUseCase(get()) }
    single { GetQuestsUseCase(get()) }
    single { GetSkillsUseCase(get()) }
    single { GetClassesUseCase(get()) }
    single { GetNpcsUseCase(get()) }
}

fun viewModelModule(): Module = module {
    factory { ItemsViewModel(get(), get()) }
    factory { QuestsViewModel(get()) }
    factory { SkillsViewModel(get()) }
    factory { ClassesViewModel(get()) }
    factory { NpcsViewModel(get()) }
    factory { SettingsViewModel(get()) }
    factory { ItemDetailViewModel(get()) }
    factory { QuestDetailViewModel(get()) }
    factory { SkillDetailViewModel(get()) }
    factory { ClassDetailViewModel(get()) }
    factory { NpcDetailViewModel(get()) }
}
