package com.zx_tole.lineage2_guide.server.data

import com.zx_tole.lineage2_guide.server.dto.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStream
import java.util.zip.GZIPInputStream

// Формат конвертированных JSON файлов
@Serializable
data class ConvertedItem(
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

@Serializable
data class ConvertedSkill(
    val id: Long,
    val name: String,
    val classRestriction: String? = null,
    val level: Int,
    val type: String,
    val description: String?,
    val cooldown: Int?,
    val iconUrl: String?,
    val manaCost: Int?,
    val range: String?
)

@Serializable
data class ConvertedNpc(
    val id: Long,
    val name: String,
    val type: String,
    val location: String? = null,
    val description: String?,
    val relatedQuestIds: List<Long> = emptyList(),
    val iconUrl: String?
)

@Serializable
data class ConvertedQuest(
    val id: Long,
    val name: String,
    val npcId: Long?,
    val startLevel: Int? = 1,
    val reward: List<ConvertedReward>,
    val description: String?,
    val type: String?,
    val prerequisites: List<Long> = emptyList(),
    val progressStatus: String?
)

@Serializable
data class ConvertedReward(
    val type: String,
    val value: Int
)

@Serializable
data class ConvertedClass(
    val id: Long,
    val name: String,
    val race: String,
    val subClasses: List<String>,
    val description: String?,
    val iconUrl: String?,
    val baseStats: Map<String, Int> = emptyMap()
)

object RealDataLoader {
    
    private fun sanitize(s: String?): String? {
        return s?.replace("\n", " ")?.replace("\r", " ")?.replace("\t", " ")?.replace(Regex("[\\x00-\\x1f]"), "")
    }
    
    private fun normalizeRarity(rarity: String): String {
        return when (rarity.uppercase()) {
            "LEGENDARY", "S" -> "Legendary"
            "EPIC", "A" -> "Epic"
            "RARE", "B" -> "Rare"
            "UNCOMMON", "C" -> "Uncommon"
            "COMMON", "D" -> "Common"
            else -> "Common"
        }
    }
    
    private fun normalizeType(type: String): String {
        return when (type.uppercase()) {
            "WEAPON" -> "Weapon"
            "ARMOR" -> "Armor"
            "CONSUMABLE" -> "Consumable"
            "SCROLL" -> "Scroll"
            "MATERIAL" -> "Material"
            "QUEST" -> "Quest"
            "ETC" -> "Etc"
            else -> type
        }
    }
    
    private fun generateLocalIconUrl(entityType: String, entityId: Long): String {
        return "http://localhost:8080/api/v1/icons/$entityType/$entityId"
    }
    
    private fun generateSkillIconUrl(skillId: Long, skillLevel: Int): String {
        val iconFile = "IL_skill_${skillId.toString().padStart(4, '0')}_${skillLevel}.png"
        return "https://raw.githubusercontent.com/cuteshaun/lineage2-api/master/data/generated/interlude/icons/$iconFile"
    }
    
    private fun generateNpcIconUrl(npcId: Long): String {
        val iconFile = "IL_npc_${npcId.toString().padStart(6, '0')}.png"
        return "https://raw.githubusercontent.com/cuteshaun/lineage2-api/master/data/generated/interlude/icons/$iconFile"
    }
    
    private fun generateQuestIconUrl(questId: Long): String {
        val iconFile = "IL_quest_${questId.toString().padStart(4, '0')}.png"
        return "https://raw.githubusercontent.com/cuteshaun/lineage2-api/master/data/generated/interlude/icons/$iconFile"
    }
    
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }
    
    fun loadItems(): List<ItemDto> {
        try {
            val inputStream = RealDataLoader::class.java.classLoader
                .getResourceAsStream("items.json.gz") ?: return emptyList()
            
            val gzipStream = GZIPInputStream(inputStream)
            val jsonString = BufferedReader(gzipStream.bufferedReader()).use { it.readText() }
            gzipStream.close()
            
            val convertedItems = json.decodeFromString<List<ConvertedItem>>(jsonString)
            
            return convertedItems.map { item ->
                ItemDto(
                    id = item.id,
                    name = sanitize(item.name) ?: "",
                    classRestriction = item.classRestriction,
                    level = item.level,
                    type = normalizeType(item.type),
                    rarity = normalizeRarity(item.rarity),
                    location = item.location,
                    dropInfo = item.dropInfo,
                    stats = item.stats.map { MapEntry(it.key, it.value) },
                    description = sanitize(item.description),
                    iconUrl = generateLocalIconUrl("items", item.id)
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return emptyList()
        }
    }
    
    fun loadSkills(): List<SkillDto> {
        try {
            val inputStream = RealDataLoader::class.java.classLoader
                .getResourceAsStream("skills.json.gz") ?: return emptyList()
            
            val gzipStream = GZIPInputStream(inputStream)
            val jsonString = BufferedReader(gzipStream.bufferedReader()).use { it.readText() }
            gzipStream.close()
            
            val convertedSkills = json.decodeFromString<List<ConvertedSkill>>(jsonString)
            
            return convertedSkills.map { skill ->
                SkillDto(
                    id = skill.id,
                    name = sanitize(skill.name) ?: "",
                    classRestriction = skill.classRestriction ?: "All",
                    level = skill.level,
                    type = skill.type,
                    description = sanitize(skill.description),
                    cooldown = skill.cooldown,
                    iconUrl = generateLocalIconUrl("skills", skill.id),
                    manaCost = skill.manaCost,
                    range = skill.range
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return emptyList()
        }
    }
    
    fun loadNpcs(): List<NpcDto> {
        try {
            val inputStream = RealDataLoader::class.java.classLoader
                .getResourceAsStream("npcs.json.gz") ?: return emptyList()
            
            val gzipStream = GZIPInputStream(inputStream)
            val jsonString = BufferedReader(gzipStream.bufferedReader()).use { it.readText() }
            gzipStream.close()
            
            val convertedNpcs = json.decodeFromString<List<ConvertedNpc>>(jsonString)
            
            return convertedNpcs.map { npc ->
                NpcDto(
                    id = npc.id,
                    name = sanitize(npc.name) ?: "Unknown NPC",
                    type = npc.type,
                    location = npc.location ?: "Unknown",
                    description = sanitize(npc.description),
                    relatedQuestIds = npc.relatedQuestIds,
                    iconUrl = generateLocalIconUrl("npcs", npc.id)
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return emptyList()
        }
    }
    
    fun loadQuests(): List<QuestDto> {
        try {
            val inputStream = RealDataLoader::class.java.classLoader
                .getResourceAsStream("quests.json.gz") ?: return emptyList()
            
            val gzipStream = GZIPInputStream(inputStream)
            val jsonString = BufferedReader(gzipStream.bufferedReader()).use { it.readText() }
            gzipStream.close()
            
            val convertedQuests = json.decodeFromString<List<ConvertedQuest>>(jsonString)
            
            return convertedQuests.map { quest ->
                QuestDto(
                    id = quest.id,
                    name = sanitize(quest.name) ?: "",
                    npcId = quest.npcId,
                    startLevel = quest.startLevel ?: 1,
                    reward = quest.reward.map { RewardDto(it.type, it.value) },
                    description = sanitize(quest.description),
                    type = quest.type ?: "Main",
                    prerequisites = quest.prerequisites,
                    progressStatus = quest.progressStatus
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return emptyList()
        }
    }
    
    fun loadClasses(): List<ClassDto> {
        try {
            val inputStream = RealDataLoader::class.java.classLoader
                .getResourceAsStream("classes.json.gz") ?: return emptyList()
            
            val gzipStream = GZIPInputStream(inputStream)
            val jsonString = BufferedReader(gzipStream.bufferedReader()).use { it.readText() }
            gzipStream.close()
            
            val convertedClasses = json.decodeFromString<List<ConvertedClass>>(jsonString)
            
            return convertedClasses.map { cls ->
                ClassDto(
                    id = cls.id,
                    name = sanitize(cls.name) ?: "",
                    race = cls.race,
                    subClasses = cls.subClasses,
                    description = sanitize(cls.description),
                    iconUrl = generateLocalIconUrl("classes", cls.id),
                    baseStats = cls.baseStats.map { MapEntry(it.key, it.value) }
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return emptyList()
        }
    }
}
