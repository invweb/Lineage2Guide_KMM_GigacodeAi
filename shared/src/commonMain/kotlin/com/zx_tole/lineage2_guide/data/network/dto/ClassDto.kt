package com.zx_tole.lineage2_guide.data.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class ClassesResponse(
    val classes: List<ClassDto>,
    val total: Int? = null,
    val offset: Int = 0,
    val limit: Int = 50
)

@Serializable
data class ClassResponse(
    val classData: ClassDto
)

@Serializable
data class ClassDto(
    val id: Long,
    val name: String,
    val race: String,
    val subClasses: List<String>,
    val description: String?,
    val iconUrl: String?,
    val baseStatsList: List<MapEntryDto> = emptyList()
) {
    val baseStats: Map<String, Int>
        get() = baseStatsList.associate { it.key to it.value }
}

@Serializable
data class MapEntryDto(
    val key: String,
    val value: Int
)
