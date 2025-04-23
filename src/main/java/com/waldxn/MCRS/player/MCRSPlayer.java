package com.waldxn.MCRS.player;

import com.waldxn.MCRS.skill.core.Skill;
import com.waldxn.MCRS.skill.core.SkillFactory;
import com.waldxn.MCRS.skill.core.SkillType;
import org.bukkit.Bukkit;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MCRSPlayer {

    private final HashMap<SkillType, Skill> skills = new HashMap<>();

    private final UUID uuid;

    public MCRSPlayer(UUID uuid) {
        this.uuid = uuid;
    }

    public Skill getSkill(SkillType type) {
        return skills.getOrDefault(type, SkillFactory.createSkill(type));
    }

    public HashMap<SkillType, Skill> getSkills() {
        return skills;
    }

    public String getName() {
        String name = Bukkit.getOfflinePlayer(uuid).getName();
        return name != null ? name : "Unknown Player";
    }

    public UUID getUUID() {
        return uuid;
    }

    public void setSkills(HashMap<SkillType, Skill> newSkills) {
        skills.clear();
        for (Map.Entry<SkillType, Skill> entry : newSkills.entrySet()) {
            if (entry.getKey() != null && entry.getValue() != null) {
                skills.put(entry.getKey(), entry.getValue());
            } else {
                Bukkit.getLogger().warning("[MCRS] Invalid skill entry in setSkills() for " + uuid);            }
        }
    }

    // Utility method to reset skills
    public void initializeSkills() {
        for (SkillType type : SkillType.values()) {
            skills.put(type, SkillFactory.createSkill(type));
        }
    }

    public org.bukkit.entity.Player getBukkitPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public boolean isOnline() {
        return getBukkitPlayer() != null;
    }
}
