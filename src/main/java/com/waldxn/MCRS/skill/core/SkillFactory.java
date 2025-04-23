package com.waldxn.MCRS.skill.core;

import com.waldxn.MCRS.skill.skills.*;

public class SkillFactory {

    public static Skill createSkill(SkillType type) {
        return switch (type) {
            case AGILITY -> new Agility();
            case CONSTRUCTION -> new Construction();
            case COOKING -> new Cooking();
            case CRAFTING -> new Crafting();
            case FARMING -> new Farming();
            case FIREMAKING -> new Firemaking();
            case FISHING -> new Fishing();
            case FLETCHING -> new Fletching();
            case HERBLORE -> new Herblore();
            case HITPOINTS -> new Hitpoints();
            case HUNTER -> new Hunter();
            case MAGIC -> new Magic();
            case MELEE -> new Melee();
            case MINING -> new Mining();
            case PRAYER -> new Prayer();
            case RANGED -> new Ranged();
            case RUNECRAFTING -> new Runecrafting();
            case SLAYER -> new Slayer();
            case SMITHING -> new Smithing();
            case THIEVING -> new Thieving();
            case WOODCUTTING -> new Woodcutting();
        };
    }
}
