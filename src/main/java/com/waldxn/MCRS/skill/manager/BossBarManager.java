package com.waldxn.MCRS.skill.manager;

import com.waldxn.MCRS.MCRS;
import com.waldxn.MCRS.skill.core.SkillType;
import com.waldxn.MCRS.skill.core.ExperienceUtil;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BossBarManager {

    private static final Map<UUID, BossBar> activeBossBars = new HashMap<>();
    private static final Map<UUID, BukkitTask> hideTimers = new HashMap<>();

    private static final MCRS mcrs = MCRS.getPlugin(MCRS.class);

    public static void showXPBar(Player bukkitPlayer, SkillType skillType, double currentXP) {
        if (skillType == SkillType.HITPOINTS) return; // Hides the hitpoints skill bar to avoid overlap with other combat skills

        UUID uuid = bukkitPlayer.getUniqueId();

        int currentLevel = ExperienceUtil.getLevelForXP(currentXP);
        double xpAtCurrentLevel = ExperienceUtil.getXPForLevel(currentLevel);
        double xpAtNextLevel = ExperienceUtil.getXPForLevel(currentLevel + 1);

        double progress = (currentXP - xpAtCurrentLevel) / (xpAtNextLevel - xpAtCurrentLevel);
        progress = Math.min(Math.max(progress, 0), 1.0);

        String title = skillType.getName() + ": " + (int) currentXP + " / " + (int) ExperienceUtil.getXPForLevel(ExperienceUtil.getLevelForXP(currentXP) + 1) + " XP | Level: " + currentLevel;

        BossBar bar = activeBossBars.get(uuid);
        if (bar == null) {
            //TODO: Add custom color in config
            bar = Bukkit.createBossBar(title, BarColor.WHITE, BarStyle.SOLID);
            activeBossBars.put(uuid, bar);
            bar.addPlayer(bukkitPlayer);
        }

        BossBar finalBar = bar;

        bar.setProgress(progress);
        bar.setTitle(title);
        bar.setVisible(true);

        // Cancel any previous hide task
        BukkitTask existingTask = hideTimers.get(uuid);
        if (existingTask != null) {
            existingTask.cancel();
        }

        BukkitTask newTask = Bukkit.getScheduler().runTaskLater(mcrs, () -> {
            finalBar.setVisible(false);
            hideTimers.remove(uuid);
        }, 100L); // 5 seconds

        hideTimers.put(uuid, newTask);
    }

}
