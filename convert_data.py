#!/usr/bin/env python3
"""Convert lineage2-api JSON to server format"""
import json
import sys

# Enums mapping
ITEM_TYPE_MAP = {
    "weapon": "WEAPON",
    "armor": "ARMOR",
    "armor_h": "HELMET",
    "armor_glove": "GLOVES",
    "armor_boots": "BOOTS",
    "armor_shield": "SHIELD",
    "etcitem": "OTHER",
    "scroll_lesson": "SCROLL",
    "scroll_enchant_armor": "SCROLL",
    "scroll_enchant_weapon": "SCROLL",
    "potion": "CONSUMABLE",
    "potions": "CONSUMABLE",
    "pill": "CONSUMABLE",
    "decoction": "CONSUMABLE",
    "seed": "MATERIAL",
    "material": "MATERIAL",
    "treasure_box": "OTHER",
    "jewelry": "JEWELRY",
    "ring": "JEWELRY",
    "earring": "JEWELTY",
    "amulet": "JEWELRY",
    "necklace": "JEWELRY",
    "lyre": "OTHER",
    "coin": "MATERIAL",
    "card": "OTHER",
    "etc": "OTHER",
}

RARITY_MAP = {
    "none": "COMMON",
    "normal": "COMMON",
    "normal_plus": "UNCOMMON",
    "epic": "EPIC",
    "legend": "LEGENDARY",
    "legendary": "LEGENDARY",
    "divine": "DIVINE",
    "high_grade": "LEGENDARY",
    "low_grade": "COMMON",
    "middle_grade": "UNCOMMON",
}

SKILL_TYPE_MAP = {
    "OP_ACTIVE": "ACTIVE",
    "OP_PASSIVE": "PASSIVE",
}

NPC_TYPE_MAP = {
    "L2Npc": "OTHER",
    "L2Merchant": "MERCHANT",
    "L2Monster": "MONSTER",
    "L2BossMonster": "BOSS",
    "Pet": "MONSTER",
    "L2QuestNpc": "QUEST_GIVER",
    "L2Teacher": "TEACHER",
    "L2CastleTeleporter": "OTHER",
    "L2SiegeGuard": "OTHER",
    "L2Guard": "OTHER",
    "L2Pc": "OTHER",
    "L2SuperAcceptor": "QUEST_GIVER",
    "L2GrandBoss": "BOSS",
}

QUEST_TYPE_MAP = {
    "MAIN": "MAIN",
    "SIDE": "SIDE",
    "ARENA": "ARENA",
}

QUEST_PROGRESS_MAP = {
    "NOT_STARTED": "NOT_STARTED",
    "IN_PROGRESS": "IN_PROGRESS",
    "COMPLETED": "COMPLETED",
}


def convert_items(input_path, output_path):
    with open(input_path, 'r', encoding='utf-8') as f:
        raw_items = json.load(f)
    
    converted = []
    for item in raw_items:
        rarity = RARITY_MAP.get(item.get("grade", "none"), "COMMON")
        item_type = ITEM_TYPE_MAP.get(item.get("type", "etcitem"), "OTHER")
        
        # Build stats
        stats = {}
        if item.get("pAtk"):
            stats["pAtk"] = item["pAtk"]
        if item.get("mAtk"):
            stats["mAtk"] = item["mAtk"]
        if item.get("pDef"):
            stats["pDef"] = item["pDef"]
        if item.get("mDef"):
            stats["mDef"] = item["mDef"]
        if item.get("rCrit"):
            stats["crit"] = item["rCrit"]
        if item.get("pAtkSpd"):
            stats["atkSpd"] = item["pAtkSpd"]
        
        converted.append({
            "id": item["id"],
            "name": item["name"],
            "classRestriction": None,
            "level": item.get("level", 1),
            "type": item_type,
            "rarity": rarity,
            "location": None,
            "dropInfo": item.get("source", {}).get("chronicle"),
            "stats": stats,
            "description": item.get("description"),
            "iconUrl": None
        })
    
    with open(output_path, 'w', encoding='utf-8') as f:
        json.dump(converted, f, ensure_ascii=False, indent=2)
    
    print(f"Items: {len(raw_items)} -> {len(converted)}")


def convert_skills(input_path, output_path):
    with open(input_path, 'r', encoding='utf-8') as f:
        raw_skills = json.load(f)
    
    converted = []
    for skill in raw_skills:
        skill_type = SKILL_TYPE_MAP.get(skill.get("operateType", "OP_ACTIVE"), "ACTIVE")
        
        converted.append({
            "id": skill["id"],
            "name": skill["name"],
            "classRestriction": None,
            "level": skill.get("level", 1),
            "type": skill_type,
            "description": skill.get("description"),
            "cooldown": skill.get("reuseDelay"),
            "iconUrl": None,
            "manaCost": skill.get("mpConsume"),
            "range": str(skill.get("castRange", "")) if skill.get("castRange") else None
        })
    
    with open(output_path, 'w', encoding='utf-8') as f:
        json.dump(converted, f, ensure_ascii=False, indent=2)
    
    print(f"Skills: {len(raw_skills)} -> {len(converted)}")


def convert_npcs(input_path, output_path):
    with open(input_path, 'r', encoding='utf-8') as f:
        raw_npcs = json.load(f)
    
    converted = []
    for npc in raw_npcs:
        npc_type = NPC_TYPE_MAP.get(npc.get("npcType", "L2Npc"), "OTHER")
        
        converted.append({
            "id": npc["id"],
            "name": npc["name"],
            "type": npc_type,
            "location": None,
            "description": npc.get("title"),
            "relatedQuestIds": [],
            "iconUrl": None
        })
    
    with open(output_path, 'w', encoding='utf-8') as f:
        json.dump(converted, f, ensure_ascii=False, indent=2)
    
    print(f"NPCs: {len(raw_npcs)} -> {len(converted)}")


def convert_quests(input_path, output_path):
    with open(input_path, 'r', encoding='utf-8') as f:
        raw_quests = json.load(f)
    
    converted = []
    for quest in raw_quests:
        # Get rewards
        rewards = []
        rewards_data = quest.get("rewards", {})
        if isinstance(rewards_data, dict):
            for reward_type, items in rewards_data.items():
                if isinstance(items, list):
                    for item in items:
                        if reward_type == "adena":
                            rewards.append({"type": "ADENA", "value": item.get("count", 0)})
                        elif reward_type == "exp":
                            rewards.append({"type": "EXP", "value": item.get("count", 0)})
                        elif reward_type == "sp":
                            rewards.append({"type": "SP", "value": item.get("count", 0)})
                        elif reward_type == "items":
                            for it in items:
                                rewards.append({"type": "ITEM", "value": it.get("count", 0)})
        
        converted.append({
            "id": quest["id"],
            "name": quest["name"],
            "npcId": quest.get("startNpcIds", [None])[0] if quest.get("startNpcIds") else None,
            "startLevel": quest.get("levelMin", 1),
            "reward": rewards,
            "description": None,
            "type": "SIDE",
            "prerequisites": [],
            "progressStatus": "NOT_STARTED"
        })
    
    with open(output_path, 'w', encoding='utf-8') as f:
        json.dump(converted, f, ensure_ascii=False, indent=2)
    
    print(f"Quests: {len(raw_quests)} -> {len(converted)}")


def convert_classes(input_path, output_path):
    with open(input_path, 'r', encoding='utf-8') as f:
        raw_classes = json.load(f)
    
    converted = []
    for cls in raw_classes:
        sub_classes = []
        if cls.get("skills"):
            # Extract unique class types from skills
            pass
        
        converted.append({
            "id": cls["id"],
            "name": cls["name"],
            "race": cls.get("race", "All"),
            "subClasses": sub_classes,
            "description": None,
            "iconUrl": None,
            "baseStats": {}
        })
    
    with open(output_path, 'w', encoding='utf-8') as f:
        json.dump(converted, f, ensure_ascii=False, indent=2)
    
    print(f"Classes: {len(raw_classes)} -> {len(converted)}")


if __name__ == "__main__":
    base = "/tmp"
    convert_items(f"{base}/items.json", f"{base}/items_converted.json")
    convert_skills(f"{base}/skills.json", f"{base}/skills_converted.json")
    convert_npcs(f"{base}/npcs.json", f"{base}/npcs_converted.json")
    convert_quests(f"{base}/quests.json", f"{base}/quests_converted.json")
    convert_classes(f"{base}/classes.json", f"{base}/classes_converted.json")
    print("\nAll files converted!")
