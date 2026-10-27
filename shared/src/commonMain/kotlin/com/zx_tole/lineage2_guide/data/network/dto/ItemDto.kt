package com.zx_tole.lineage2_guide.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class ItemsResponse(
    val items: List<ItemDto>,
    val total: Int? = null,
    val offset: Int = 0,
    val limit: Int = 50
)

@Serializable
data class ItemResponse(
    val item: ItemDto
)

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
    val stats: Map<String, Int> = emptyMap(),
    val description: String?,
    val iconUrl: String?
)
