package com.waldxn.MCRS.player;

import com.waldxn.MCRS.skill.core.Skill;
import com.waldxn.MCRS.skill.core.SkillFactory;
import com.waldxn.MCRS.skill.core.SkillType;
import com.waldxn.MCRS.util.LogUtil;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MCRSPlayer {

    private final HashMap<SkillType, Skill> skills = new HashMap<>();

    private final UUID uuid;
    private boolean dirty = false;

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
                LogUtil.warning( "Invalid skill entry in setSkills() for " + uuid);
            }
        }
    }

    public void setSkillLevel(SkillType type, int level) {
        Skill skill = skills.get(type);
        if (skill != null) {
            skill.setLevel(level);
        } else {
            LogUtil.warning("Attempted to set level for non-existent skill: " + type);
        }
    }

    // Utility method to reset skills
    public void initializeSkills() {
        for (SkillType type : SkillType.values()) {
            if (type == SkillType.HITPOINTS) {
                Skill hitpoints = SkillFactory.createSkill(type);
                hitpoints.setInitialExperience(1154);
                skills.put(type, hitpoints);
                continue;
            }
            skills.put(type, SkillFactory.createSkill(type));
        }
    }

    public void markDirty() {
        this.dirty = true;
    }

    public void clearDirty() {
        this.dirty = false;
    }

    public boolean isDirty() {
        return dirty;
    }

    public org.bukkit.entity.Player getBukkitPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public boolean isOnline() {
        return getBukkitPlayer() != null;
    }
}
