package com.waldxn.MCRS.skill.core;

import com.waldxn.MCRS.util.ChatUtil;

import java.util.List;

public abstract class Skill {

    private final SkillType type;
    private int level;
    private double experience;

    public Skill(SkillType type) {
        this.type = type;
        this.experience = 0;
        this.level = 1;
    }

    public String getName() {
        return type.getName();
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
        this.experience = ExperienceUtil.getXPForLevel(level);
    }

    public double getExperience() {
        return experience;
    }

    public boolean addExperience(double amount) {
        int oldLevel = getLevel();
        experience += amount;
        int newLevel = ExperienceUtil.getLevelForXP(experience);

        boolean leveledUp = newLevel > oldLevel;
        level = newLevel;

        return leveledUp;
    }

    public List<String> getHoverInfo() {
        int level = getLevel();
        double experience = getExperience();
        double nextLevelExperience = ExperienceUtil.getXPForLevel(level + 1);
        double currentLevelExperience = ExperienceUtil.getXPForLevel(level);
        int neededExperience = (int) (nextLevelExperience - currentLevelExperience);

        if (level == 99) {
            return List.of(
                    ChatUtil.color("&6Level: " + level),
                    ChatUtil.color("&eXP: " + (int) experience)
            );
        }

        return List.of(
                ChatUtil.color("&6Level: " + level),
                ChatUtil.color("&eXP: " + (int)(experience - currentLevelExperience) + " / " + neededExperience)
        );
    }

    public void setInitialExperience(double experience) {
        this.experience = experience;
        this.level = ExperienceUtil.getLevelForXP(experience);
    }
}
