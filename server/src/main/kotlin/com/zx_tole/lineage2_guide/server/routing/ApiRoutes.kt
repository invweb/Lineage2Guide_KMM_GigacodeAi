package com.zx_tole.lineage2_guide.server.routing

import com.zx_tole.lineage2_guide.server.data.ServerMockData
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Routing.apiRoutes() {

    get("/api/v1/items") {
        val rarity = call.request.queryParameters["rarity"]
        val type = call.request.queryParameters["type"]
        val search = call.request.queryParameters["search"]
        val offset = call.request.queryParameters["offset"]?.toIntOrNull() ?: 0
        val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 50

        var filtered = ServerMockData.items

        search?.let { query ->
            filtered = filtered.filter { item ->
                item.name.lowercase().contains(query) ||
                    item.description?.lowercase()?.contains(query) == true
            }
        }

        rarity?.let { r ->
            filtered = filtered.filter { it.rarity.equals(r, ignoreCase = true) }
        }

        type?.let { t ->
            filtered = filtered.filter { it.type.equals(t, ignoreCase = true) }
        }

        val total = filtered.size
        val paged = filtered.drop(offset).take(limit)
        
        val json = "{\"items\":[" + paged.joinToString(",") { itemToJson(it) } + "],\"total\":$total,\"offset\":$offset,\"limit\":$limit}"
        call.respondText(json, ContentType.Application.Json)
    }

    get("/api/v1/items/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
        val item = ServerMockData.items.find { it.id == id }
        if (item != null) {
            call.respondText("{\"item\":${itemToJson(item)}}", ContentType.Application.Json)
        } else {
            call.respondText("""{"error":"Item not found"}""", ContentType.Application.Json, HttpStatusCode.NotFound)
        }
    }

    get("/api/v1/quests") {
        val questType = call.request.queryParameters["type"]
        val search = call.request.queryParameters["search"]
        val offset = call.request.queryParameters["offset"]?.toIntOrNull() ?: 0
        val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 50

        var filtered = ServerMockData.quests

        search?.let { query ->
            filtered = filtered.filter { quest ->
                quest.name.lowercase().contains(query) ||
                    quest.description?.lowercase()?.contains(query) == true
            }
        }

        questType?.let { t ->
            filtered = filtered.filter { it.type.equals(t, ignoreCase = true) }
        }

        val total = filtered.size
        val paged = filtered.drop(offset).take(limit)
        
        val json = "{\"quests\":[" + paged.joinToString(",") { questToJson(it) } + "],\"total\":$total,\"offset\":$offset,\"limit\":$limit}"
        call.respondText(json, ContentType.Application.Json)
    }

    get("/api/v1/quests/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
        val quest = ServerMockData.quests.find { it.id == id }
        if (quest != null) {
            call.respondText("{\"quest\":${questToJson(quest)}}", ContentType.Application.Json)
        } else {
            call.respondText("""{"error":"Quest not found"}""", ContentType.Application.Json, HttpStatusCode.NotFound)
        }
    }

    get("/api/v1/skills") {
        val classRestriction = call.request.queryParameters["classRestriction"]
        val skillType = call.request.queryParameters["type"]
        val offset = call.request.queryParameters["offset"]?.toIntOrNull() ?: 0
        val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 50

        var filtered = ServerMockData.skills

        classRestriction?.let { c ->
            filtered = filtered.filter { it.classRestriction.equals(c, ignoreCase = true) }
        }

        skillType?.let { t ->
            filtered = filtered.filter { it.type.equals(t, ignoreCase = true) }
        }

        val total = filtered.size
        val paged = filtered.drop(offset).take(limit)
        
        val json = "{\"skills\":[" + paged.joinToString(",") { skillToJson(it) } + "],\"total\":$total,\"offset\":$offset,\"limit\":$limit}"
        call.respondText(json, ContentType.Application.Json)
    }

    get("/api/v1/skills/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
        val skill = ServerMockData.skills.find { it.id == id }
        if (skill != null) {
            call.respondText("{\"skill\":${skillToJson(skill)}}", ContentType.Application.Json)
        } else {
            call.respondText("""{"error":"Skill not found"}""", ContentType.Application.Json, HttpStatusCode.NotFound)
        }
    }

    get("/api/v1/classes") {
        val offset = call.request.queryParameters["offset"]?.toIntOrNull() ?: 0
        val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 50
        
        val total = ServerMockData.classes.size
        val paged = ServerMockData.classes.drop(offset).take(limit)
        
        val json = "{\"classes\":[" + paged.joinToString(",") { classToJson(it) } + "],\"total\":$total,\"offset\":$offset,\"limit\":$limit}"
        call.respondText(json, ContentType.Application.Json)
    }

    get("/api/v1/classes/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
        val classData = ServerMockData.classes.find { it.id == id }
        if (classData != null) {
            call.respondText("{\"class\":${classToJson(classData)}}", ContentType.Application.Json)
        } else {
            call.respondText("""{"error":"Class not found"}""", ContentType.Application.Json, HttpStatusCode.NotFound)
        }
    }

    get("/api/v1/npcs") {
        val npcType = call.request.queryParameters["type"]
        val search = call.request.queryParameters["search"]
        val offset = call.request.queryParameters["offset"]?.toIntOrNull() ?: 0
        val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 50

        var filtered = ServerMockData.npcs

        search?.let { query ->
            filtered = filtered.filter { npc ->
                npc.name.lowercase().contains(query) ||
                    npc.description?.lowercase()?.contains(query) == true
            }
        }

        npcType?.let { t ->
            filtered = filtered.filter { it.type.equals(t, ignoreCase = true) }
        }

        val total = filtered.size
        val paged = filtered.drop(offset).take(limit)
        
        val json = "{\"npcs\":[" + paged.joinToString(",") { npcToJson(it) } + "],\"total\":$total,\"offset\":$offset,\"limit\":$limit}"
        call.respondText(json, ContentType.Application.Json)
    }

    get("/api/v1/npcs/{id}") {
        val id = call.parameters["id"]?.toLongOrNull()
        val npc = ServerMockData.npcs.find { it.id == id }
        if (npc != null) {
            call.respondText("{\"npc\":${npcToJson(npc)}}", ContentType.Application.Json)
        } else {
            call.respondText("""{"error":"NPC not found"}""", ContentType.Application.Json, HttpStatusCode.NotFound)
        }
    }

    get("/api/v1/sync") {
        val json = "{\"lastSync\":${System.currentTimeMillis()},\"hasUpdates\":false,\"updatedEntities\":{}}"
        call.respondText(json, ContentType.Application.Json)
    }
}

// JSON builders

private fun q(s: String?) = if (s == null) "null" else "\"${s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "").replace("\t", "\\t")}\""

private fun itemToJson(item: com.zx_tole.lineage2_guide.server.dto.ItemDto): String = buildString {
    append("{")
    append("\"id\":${item.id},")
    append("\"name\":${q(item.name)},")
    append("\"classRestriction\":${q(item.classRestriction)},")
    append("\"level\":${item.level},")
    append("\"type\":${q(item.type)},")
    append("\"rarity\":${q(item.rarity)},")
    append("\"location\":${q(item.location)},")
    append("\"dropInfo\":${q(item.dropInfo)},")
    append("\"stats\":{${item.stats.joinToString(",") { "\"${it.key}\":${it.value}" }}},")
    append("\"description\":${q(item.description)},")
    append("\"iconUrl\":${q(item.iconUrl)}")
    append("}")
}

private fun questToJson(quest: com.zx_tole.lineage2_guide.server.dto.QuestDto): String = buildString {
    append("{")
    append("\"id\":${quest.id},")
    append("\"name\":${q(quest.name)},")
    append("\"npcId\":${if (quest.npcId != null) quest.npcId else "null"},")
    append("\"startLevel\":${quest.startLevel},")
    append("\"reward\":[${quest.reward.joinToString(",") { "{\"type\":${q(it.type)},\"value\":${it.value}}" }}],")
    append("\"description\":${q(quest.description)},")
    append("\"type\":${q(quest.type)},")
    append("\"prerequisites\":[${quest.prerequisites.joinToString(",")}],")
    append("\"progressStatus\":${q(quest.progressStatus)}")
    append("}")
}

private fun skillToJson(skill: com.zx_tole.lineage2_guide.server.dto.SkillDto): String = buildString {
    append("{")
    append("\"id\":${skill.id},")
    append("\"name\":${q(skill.name)},")
    append("\"classRestriction\":${q(skill.classRestriction)},")
    append("\"level\":${skill.level},")
    append("\"type\":${q(skill.type)},")
    append("\"description\":${q(skill.description)},")
    append("\"cooldown\":${if (skill.cooldown != null) skill.cooldown else "null"},")
    append("\"iconUrl\":${q(skill.iconUrl)},")
    append("\"manaCost\":${if (skill.manaCost != null) skill.manaCost else "null"},")
    append("\"range\":${q(skill.range)}")
    append("}")
}

private fun classToJson(classData: com.zx_tole.lineage2_guide.server.dto.ClassDto): String = buildString {
    append("{")
    append("\"id\":${classData.id},")
    append("\"name\":${q(classData.name)},")
    append("\"race\":${q(classData.race)},")
    append("\"subClasses\":[${classData.subClasses.joinToString(",") { q(it) }}],")
    append("\"description\":${q(classData.description)},")
    append("\"iconUrl\":${q(classData.iconUrl)},")
    append("\"baseStats\":[${classData.baseStats.joinToString(",") { "\"${it.key}\":${it.value}" }}]")
    append("}")
}

private fun npcToJson(npc: com.zx_tole.lineage2_guide.server.dto.NpcDto): String = buildString {
    append("{")
    append("\"id\":${npc.id},")
    append("\"name\":${q(npc.name)},")
    append("\"type\":${q(npc.type)},")
    append("\"location\":${q(npc.location)},")
    append("\"description\":${q(npc.description)},")
    append("\"relatedQuestIds\":[${npc.relatedQuestIds.joinToString(",")}],")
    append("\"iconUrl\":${q(npc.iconUrl)}")
    append("}")
}
