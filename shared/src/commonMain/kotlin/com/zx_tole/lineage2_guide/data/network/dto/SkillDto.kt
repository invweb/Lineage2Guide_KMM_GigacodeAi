package com.zx_tole.lineage2_guide.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class SkillsResponse(
    val skills: List<SkillDto>,
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
