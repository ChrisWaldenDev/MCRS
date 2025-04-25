package com.waldxn.MCRS.skill.manager;

import com.waldxn.MCRS.MCRS;
import com.waldxn.MCRS.skill.core.ExperienceUtil;
import com.waldxn.MCRS.skill.core.SkillType;
import com.waldxn.MCRS.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BossBarManager {

    private static final Map<UUID, BossBar> activeBossBars = new HashMap<>();
    private static final Map<UUID, BukkitTask> hideTimers = new HashMap<>();
    private static final Map<UUID, BukkitTask> titleResetTasks = new HashMap<>();

    private static final MCRS mcrs = MCRS.getInstance();

    public static void showXPBar(Player bukkitPlayer, SkillType skillType, double currentXP, double amountGained) {

        System.out.println("BossBar: Showing XP bar for " + bukkitPlayer.getName() +
                " | Skill: " + skillType +
                " | XP: " + currentXP);

        if (skillType == SkillType.HITPOINTS) return; // Hides the hitpoints skill bar to avoid overlap with other combat skills

        UUID uuid = bukkitPlayer.getUniqueId();

        int currentLevel = ExperienceUtil.getLevelForXP(currentXP);
        double xpAtCurrentLevel = ExperienceUtil.getXPForLevel(currentLevel);
        double xpAtNextLevel = ExperienceUtil.getXPForLevel(currentLevel + 1);

        double progress = (currentXP - xpAtCurrentLevel) / (xpAtNextLevel - xpAtCurrentLevel);
        progress = Math.min(Math.max(progress, 0), 1.0);

        DecimalFormat formatter = new DecimalFormat("#,###");
        String current = formatter.format((int) currentXP);
        String needed = formatter.format((int) xpAtNextLevel);

        String title = skillType.getName() + ": " + current + " / " + needed + " XP | Level: " + currentLevel;

        BossBar bar = activeBossBars.get(uuid);

        if (bar == null) {
            bar = Bukkit.createBossBar(title, BarColor.WHITE, BarStyle.SOLID);
            activeBossBars.put(uuid, bar);
            bar.addPlayer(bukkitPlayer);
        }

        BossBar finalBar = bar;

        String gainTitle = ChatUtil.color("&a+" + (int) amountGained + " XP &f→ " + title);

        bar.setProgress(progress);
        bar.setTitle(gainTitle);
        bar.setVisible(true);

        // Cancel any previous hide task
        BukkitTask hideTask = hideTimers.get(uuid);
        if (hideTask != null) hideTask.cancel();

        // Cancel any previous title reset task
        BukkitTask resetTask = titleResetTasks.get(uuid);
        if (resetTask != null) resetTask.cancel();

        BukkitTask newTitleReset = Bukkit.getScheduler().runTaskLater(mcrs, () -> {
            // Reset the title to the original title after 30 ticks
            //TODO: Make this a smooth transition
            finalBar.setTitle(title);
            titleResetTasks.remove(uuid);
        }, 30L); // 1.5 seconds
        titleResetTasks.put(uuid, newTitleReset);

        // Schedule a task to hide the bar after 100 ticks
        BukkitTask newHideReset = Bukkit.getScheduler().runTaskLater(mcrs, () -> {
            finalBar.setVisible(false);
            hideTimers.remove(uuid);
        }, 100L); // 5 seconds
        hideTimers.put(uuid, newHideReset);
    }
}
