package com.zx_tole.lineage2_guide.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class NpcsResponse(
    val npcs: List<NpcDto>,
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
data class SyncResponse(
    val lastSync: Long,
    val hasUpdates: Boolean,
    val updatedEntities: Map<String, Long> = emptyMap()
)
