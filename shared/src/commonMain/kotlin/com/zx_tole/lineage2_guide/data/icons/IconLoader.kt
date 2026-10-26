package com.zx_tole.lineage2_guide.data.icons

import com.zx_tole.lineage2_guide.domain.model.ItemType

/**
 * Icon loader for Lineage 2 Interlude icons from lineage2-api GitHub repository
 * Icons are loaded from: https://github.com/cuteshaun/lineage2-api
 */
object IconLoader {
    
    private const val ICONS_BASE_URL = "https://raw.githubusercontent.com/cuteshaun/lineage2-api/master/data/generated/interlude/icons"
    
    /**
     * Generate icon URL for an item by its ID
     */
    fun getItemIconUrl(itemId: Long): String {
        val iconFile = when {
            itemId < 10000 -> "IL_item_${itemId.toString().padStart(4, '0')}.png"
            itemId < 100000 -> "IL_item_${itemId.toString().padStart(5, '0')}.png"
            else -> "IL_item_${itemId.toString().padStart(6, '0')}.png"
        }
        return "$ICONS_BASE_URL/$iconFile"
    }
    
    /**
     * Generate icon URL for a skill by its ID and level
     */
    fun getSkillIconUrl(skillId: Long, skillLevel: Int): String {
        val iconFile = "IL_skill_${skillId.toString().padStart(4, '0')}_${skillLevel}.png"
        return "$ICONS_BASE_URL/$iconFile"
    }
    
    /**
     * Generate icon URL for a skill by ID only (uses level 1)
     */
    fun getSkillIconUrl(skillId: Long): String {
        val iconFile = "IL_skill_${skillId.toString().padStart(4, '0')}_1.png"
        return "$ICONS_BASE_URL/$iconFile"
    }
    
    /**
     * Generate icon URL for an NPC by its ID
     */
    fun getNpcIconUrl(npcId: Long): String {
        val iconFile = "IL_npc_${npcId.toString().padStart(6, '0')}.png"
        return "$ICONS_BASE_URL/$iconFile"
    }
    
    /**
     * Generate icon URL for a class by name
     */
    fun getClassIconUrl(className: String): String {
        val iconFile = "IL_class_${className.lowercase().replace(" ", "_")}.png"
        return "$ICONS_BASE_URL/$iconFile"
    }
    
    /**
     * Generate icon URL for a quest
     */
    fun getQuestIconUrl(questId: Long): String {
        val iconFile = "IL_quest_${questId.toString().padStart(4, '0')}.png"
        return "$ICONS_BASE_URL/$iconFile"
    }
    
    /**
     * Get the base URL for all icons
     */
    fun getBaseUrl(): String = ICONS_BASE_URL
    
    /**
     * Check if an icon URL is valid
     */
    fun isValidIconUrl(url: String?): Boolean {
        return url != null && url.startsWith("http") && url.contains("lineage2-api")
    }
    
    /**
     * Get fallback icon filename for an item type
     */
    fun getFallbackIconForType(type: ItemType): String {
        return when (type) {
            ItemType.WEAPON -> "IL_weapon_sword.png"
            ItemType.ARMOR -> "IL_armor_chest.png"
            ItemType.BOOTS -> "IL_armor_boots.png"
            ItemType.GLOVES -> "IL_armor_gloves.png"
            ItemType.HELMET -> "IL_armor_helmet.png"
            ItemType.SHIELD -> "IL_armor_shield.png"
            ItemType.POTION -> "IL_consumable_potion.png"
            ItemType.SCROLL -> "IL_scroll_enchant.png"
            ItemType.MATERIAL -> "IL_material_crystal.png"
            ItemType.JEWELRY -> "IL_jewelry_ring.png"
            ItemType.OTHER -> "IL_etc_unknown.png"
        }
    }
}
