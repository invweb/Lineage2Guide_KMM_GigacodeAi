package com.zx_tole.lineage2_guide.di

import com.zx_tole.lineage2_guide.data.local.Lineage2Database
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
import com.zx_tole.lineage2_guide.ui.skills.SkillDetailViewModel
import com.zx_tole.lineage2_guide.ui.skills.SkillsViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

fun sharedModule(baseUrl: String): List<Module> = listOf(
    networkModule(baseUrl),
    mapperModule(),
    repositoryModule(),
    viewModelModule(),
    useCaseModule()
)

fun getIOSModule(baseUrl: String): List<Module> = sharedModule(baseUrl)

fun networkModule(baseUrl: String): Module = module {
    single { createHttpClient(baseUrl) }
    single { Lineage2ApiService(get(), baseUrl) }
}

fun mapperModule(): Module = module {
    single { ItemMapper() }
    single { QuestMapper() }
    single { SkillMapper() }
    single { ClassMapper() }
    single { NpcMapper() }
}

fun repositoryModule(): Module = module {
    single { ItemsRepositoryImpl(get(), get()) }
    single { QuestsRepositoryImpl(get(), get()) }
    single { SkillsRepositoryImpl(get(), get()) }
    single { ClassesRepositoryImpl(get(), get()) }
    single { NpcsRepositoryImpl(get(), get()) }
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
    factory { ItemsViewModel(get()) }
    factory { QuestsViewModel(get()) }
    factory { SkillsViewModel(get()) }
    factory { ClassesViewModel(get()) }
    factory { NpcsViewModel(get()) }
    factory { ItemDetailViewModel(get()) }
    factory { QuestDetailViewModel(get()) }
    factory { SkillDetailViewModel(get()) }
    factory { ClassDetailViewModel(get()) }
    factory { NpcDetailViewModel(get()) }
}
