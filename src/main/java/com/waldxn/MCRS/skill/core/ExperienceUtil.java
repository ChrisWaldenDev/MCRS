package com.waldxn.MCRS.skill.core;

public class ExperienceUtil {

    public static double getXPForLevel(int level) {
        double xp = 0;
        for (int i = 1; i < level; i++) {
            xp += Math.floor((i + 300 * Math.pow(2, i / 7.0)) / 4);
        }
        return xp;
    }

    public static int getLevelForXP(double xp) {
        for (int level = 1; level <= 99; level++) {
            if (getXPForLevel(level + 1) > xp) {
                return level;
            }
        }
        return 99; // Max level
    }
}
