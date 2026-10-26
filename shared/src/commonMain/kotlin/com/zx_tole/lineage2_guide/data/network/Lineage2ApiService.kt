package com.zx_tole.lineage2_guide.data.network

import com.zx_tole.lineage2_guide.data.network.dto.*
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class Lineage2ApiService(
    private val httpClient: HttpClient,
    private val baseUrl: String
) {
    suspend fun getItems(
        classRestriction: String? = null,
        minLevel: Int? = null,
        maxLevel: Int? = null,
        type: String? = null,
        rarity: String? = null,
        location: String? = null,
        search: String? = null,
        offset: Int = 0,
        limit: Int = 20
    ): ItemsResponse {
        return httpClient.get("$baseUrl/api/v1/items") {
            classRestriction?.let { parameter("classRestriction", it) }
            minLevel?.let { parameter("minLevel", it) }
            maxLevel?.let { parameter("maxLevel", it) }
            type?.let { parameter("type", it) }
            rarity?.let { parameter("rarity", it) }
            location?.let { parameter("location", it) }
            search?.let { parameter("search", it) }
            parameter("offset", offset)
            parameter("limit", limit)
        }.body()
    }

    suspend fun getItemById(id: Long): ItemResponse {
        return httpClient.get("$baseUrl/api/v1/items/$id").body()
    }

    suspend fun getQuests(
        type: String? = null,
        startLevel: Int? = null,
        npcId: Long? = null,
        offset: Int = 0,
        limit: Int = 50
    ): QuestsResponse {
        return httpClient.get("$baseUrl/api/v1/quests") {
            type?.let { parameter("type", it) }
            startLevel?.let { parameter("startLevel", it) }
            npcId?.let { parameter("npcId", it) }
            parameter("offset", offset)
            parameter("limit", limit)
        }.body()
    }

    suspend fun getSkills(
        classRestriction: String? = null,
        type: String? = null,
        minLevel: Int? = null,
        maxLevel: Int? = null,
        search: String? = null,
        offset: Int = 0,
        limit: Int = 50
    ): SkillsResponse {
        return httpClient.get("$baseUrl/api/v1/skills") {
            classRestriction?.let { parameter("classRestriction", it) }
            type?.let { parameter("type", it) }
            minLevel?.let { parameter("minLevel", it) }
            maxLevel?.let { parameter("maxLevel", it) }
            search?.let { parameter("search", it) }
            parameter("offset", offset)
            parameter("limit", limit)
        }.body()
    }

    suspend fun getClasses(
        race: String? = null,
        search: String? = null,
        offset: Int = 0,
        limit: Int = 50
    ): ClassesResponse {
        return httpClient.get("$baseUrl/api/v1/classes") {
            race?.let { parameter("race", it) }
            search?.let { parameter("search", it) }
            parameter("offset", offset)
            parameter("limit", limit)
        }.body()
    }

    suspend fun getNpcs(
        type: String? = null,
        location: String? = null,
        search: String? = null,
        offset: Int = 0,
        limit: Int = 50
    ): NpcsResponse {
        return httpClient.get("$baseUrl/api/v1/npcs") {
            type?.let { parameter("type", it) }
            location?.let { parameter("location", it) }
            search?.let { parameter("search", it) }
            parameter("offset", offset)
            parameter("limit", limit)
        }.body()
    }

    suspend fun getSyncData(lastSync: Long = 0): SyncResponse {
        return httpClient.get("$baseUrl/api/v1/sync") {
            parameter("lastSync", lastSync)
        }.body()
    }
}
