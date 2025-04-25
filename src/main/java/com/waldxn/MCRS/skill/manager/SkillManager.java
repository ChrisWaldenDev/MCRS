package com.waldxn.MCRS.skill.manager;

import com.waldxn.MCRS.player.MCRSPlayer;
import com.waldxn.MCRS.skill.core.Skill;
import com.waldxn.MCRS.skill.core.SkillType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class SkillManager {

    public static void giveExperience(MCRSPlayer mcrsPlayer, SkillType type, double amount) {
        if (amount <= 0) return;

        Skill skill = mcrsPlayer.getSkill(type);
        int level = skill.getLevel();
        boolean leveledUp = skill.addExperience(amount);
        int newLevel = skill.getLevel();

        mcrsPlayer.markDirty();

        Player bukkitPlayer = Bukkit.getPlayer(mcrsPlayer.getUUID());

        if (bukkitPlayer != null) {
            BossBarManager.showXPBar(bukkitPlayer, type, skill.getExperience(), amount);
        }

        if (leveledUp && newLevel > level) {
            bukkitPlayer.sendMessage(ChatColor.BLUE + "Congratulations, you just advanced a " + type.getName() + " level.");
            bukkitPlayer.sendMessage(ChatColor.GOLD + "Your " + type.getName() + " level is now " + newLevel);
        }
    }
}