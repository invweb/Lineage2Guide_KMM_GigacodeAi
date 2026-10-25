package com.zx_tole.lineage2_guide.ui.navigation

sealed class Screen(val route: String, val title: String) {
    object Items : Screen("items", "Items")
    object Quests : Screen("quests", "Quests")
    object Skills : Screen("skills", "Skills")
    object Classes : Screen("classes", "Classes")
    object Npcs : Screen("npcs", "NPCs")
    
    companion object {
        const val ITEM_DETAIL = "item_detail/{itemId}"
        const val QUEST_DETAIL = "quest_detail/{questId}"
        const val SKILL_DETAIL = "skill_detail/{skillId}"
        const val CLASS_DETAIL = "class_detail/{classId}"
        const val NPC_DETAIL = "npc_detail/{npcId}"
    }
}
