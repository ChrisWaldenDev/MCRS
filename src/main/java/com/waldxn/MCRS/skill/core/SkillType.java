package com.waldxn.MCRS.skill.core;

import org.bukkit.Material;

public enum SkillType {
    AGILITY("Agility", Material.FEATHER),
    CONSTRUCTION("Construction", Material.BRICK),
    COOKING("Cooking", Material.CAKE),
    CRAFTING("Crafting", Material.CRAFTING_TABLE),
    FARMING("Farming", Material.DIAMOND_HOE),
    FIREMAKING("Firemaking", Material.FLINT_AND_STEEL),
    FISHING("Fishing", Material.FISHING_ROD),
    FLETCHING("Fletching", Material.ARROW),
    HERBLORE("Herblore", Material.BREWING_STAND),
    HITPOINTS("Hitpoints", Material.GOLDEN_APPLE),
    HUNTER("Hunter", Material.RABBIT_HIDE),
    MAGIC("Magic", Material.AMETHYST_SHARD),
    MELEE("Melee", Material.DIAMOND_SWORD),
    MINING("Mining", Material.DIAMOND_PICKAXE),
    PRAYER("Prayer", Material.BONE),
    RANGED("Ranged", Material.BOW),
    RUNECRAFTING("Runecrafting", Material.RESPAWN_ANCHOR),
    SLAYER("Slayer", Material.SKELETON_SKULL),
    SMITHING("Smithing", Material.ANVIL),
    THIEVING("Thieving", Material.BLACK_BUNDLE),
    WOODCUTTING("Woodcutting", Material.DIAMOND_AXE);

    private final String name;
    private final Material icon;

    SkillType(String name, Material icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public Material getIcon() {
        return icon;
    }
}
