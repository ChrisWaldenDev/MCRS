package com.waldxn.MCRS.skill.manager;

import com.waldxn.MCRS.MCRS;
import com.waldxn.MCRS.skill.core.ExperienceUtil;
import com.waldxn.MCRS.skill.core.SkillType;
import com.waldxn.MCRS.util.TextUtil;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
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

        if (skillType == SkillType.HITPOINTS)
            return; // Hides the hitpoints skill bar to avoid overlap with other combat skills

        UUID uuid = bukkitPlayer.getUniqueId();

        int currentLevel = ExperienceUtil.getLevelForXP(currentXP);
        double xpAtCurrentLevel = ExperienceUtil.getXPForLevel(currentLevel);
        double xpAtNextLevel = ExperienceUtil.getXPForLevel(currentLevel + 1);

        double progress = (currentXP - xpAtCurrentLevel) / (xpAtNextLevel - xpAtCurrentLevel);
        progress = Math.min(Math.max(progress, 0), 1.0);

        DecimalFormat formatter = new DecimalFormat("#,###");
        String current = formatter.format((int) currentXP);
        String needed = formatter.format((int) xpAtNextLevel);

        Component title = Component.text()
                .append(Component.text(skillType.getName(), NamedTextColor.WHITE))
                .append(Component.text(": "))
                .append(Component.text(current))
                .append(Component.text(" / "))
                .append(Component.text(needed))
                .append(Component.text(" XP | Level: "))
                .append(Component.text(currentLevel))
                .build();

        BossBar bar = activeBossBars.get(uuid);

        if (bar == null) {
            bar = BossBar.bossBar(
                    title,
                    (float) progress,
                    BossBar.Color.WHITE,
                    BossBar.Overlay.PROGRESS
            );
            activeBossBars.put(uuid, bar);
        }

        bukkitPlayer.showBossBar(bar);

        BossBar finalBar = bar;
        Component gainTitle = TextUtil.xpGainBossBar(amountGained, title);
        bar.name(gainTitle);
        bar.progress((float) progress);

        // Cancel any previous hide task
        BukkitTask hideTask = hideTimers.get(uuid);
        if (hideTask != null) hideTask.cancel();

        // Cancel any previous title reset task
        BukkitTask resetTask = titleResetTasks.get(uuid);
        if (resetTask != null) resetTask.cancel();

        BukkitTask newTitleReset = Bukkit.getScheduler().runTaskLater(mcrs, () -> {
            // Reset the title to the original title after 30 ticks
            finalBar.name(title);
            titleResetTasks.remove(uuid);
        }, 30L); // 1.5 seconds
        titleResetTasks.put(uuid, newTitleReset);

        // Schedule a task to hide the bar after 100 ticks
        BukkitTask newHideReset = Bukkit.getScheduler().runTaskLater(mcrs, () -> {
            bukkitPlayer.hideBossBar(finalBar);
            hideTimers.remove(uuid);
        }, 100L); // 5 seconds
        hideTimers.put(uuid, newHideReset);
    }
}
