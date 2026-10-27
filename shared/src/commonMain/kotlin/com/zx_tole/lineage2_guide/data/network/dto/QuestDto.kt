package com.zx_tole.lineage2_guide.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class QuestsResponse(
    val quests: List<QuestDto>,
    val total: Int? = null,
    val offset: Int = 0,
    val limit: Int = 50
)

@Serializable
data class QuestResponse(
    val quest: QuestDto
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
