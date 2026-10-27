package com.zx_tole.lineage2_guide.server.dto

import kotlinx.serialization.Serializable

@Serializable
data class ItemDto(
    val id: Long,
    val name: String,
    val classRestriction: String?,
    val level: Int,
    val type: String,
    val rarity: String,
    val location: String?,
    val dropInfo: String?,
    val stats: List<MapEntry> = emptyList(),
    val description: String?,
    val iconUrl: String?
)

@Serializable
data class MapEntry(
    val key: String,
    val value: Int
)

@Serializable
data class ItemsResponse(
    val items: List<ItemDto>,
    val total: Int? = null,
    val offset: Int = 0,
    val limit: Int = 50
)

@Serializable
data class QuestDto(
    val id: Long,
    val name: String,
    val npcId: Long?,
    val startLevel: Int,
    val reward: List<RewardDto>,
    val description: String?,
    val type: String,
    val prerequisites: List<Long> = emptyList(),
    val progressStatus: String?
)

@Serializable
data class RewardDto(
    val type: String,
    val value: Int
)

@Serializable
data class QuestsResponse(
    val quests: List<QuestDto>,
    val total: Int? = null,
    val offset: Int = 0,
    val limit: Int = 50
)

@Serializable
data class SkillDto(
    val id: Long,
    val name: String,
    val classRestriction: String,
    val level: Int,
    val type: String,
    val description: String?,
    val cooldown: Int?,
    val iconUrl: String?,
    val manaCost: Int?,
    val range: String?
)

@Serializable
data class SkillsResponse(
    val skills: List<SkillDto>,
    val total: Int? = null,
    val offset: Int = 0,
    val limit: Int = 50
)

@Serializable
data class ClassDto(
    val id: Long,
    val name: String,
    val race: String,
    val subClasses: List<String>,
    val description: String?,
    val iconUrl: String?,
    val baseStats: List<MapEntry> = emptyList()
)

@Serializable
data class ClassesResponse(
    val classes: List<ClassDto>,
    val total: Int? = null,
    val offset: Int = 0,
    val limit: Int = 50
)

@Serializable
data class NpcDto(
    val id: Long,
    val name: String,
    val type: String,
    val location: String,
    val description: String?,
    val relatedQuestIds: List<Long> = emptyList(),
    val iconUrl: String?
)

@Serializable
data class NpcsResponse(
    val npcs: List<NpcDto>,
    val total: Int? = null,
    val offset: Int = 0,
    val limit: Int = 50
)
