package com.waldxn.MCRS.skill.manager;

import com.waldxn.MCRS.common.util.LogUtil;
import com.waldxn.MCRS.player.MCRSPlayer;
import com.waldxn.MCRS.skill.core.Skill;
import com.waldxn.MCRS.skill.core.SkillType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SkillManager {

    private final BossBarManager bossBarManager;

    public SkillManager(BossBarManager bossBarManager) {
        this.bossBarManager = bossBarManager;
    }

    public void giveExperience(MCRSPlayer mcrsPlayer, SkillType type, double amount) {
        if (amount <= 0) return;

        Skill skill = mcrsPlayer.getSkill(type);
        int level = skill.getLevel();
        boolean leveledUp = skill.addExperience(amount);
        int newLevel = skill.getLevel();

        mcrsPlayer.markDirty();

        Player bukkitPlayer = Bukkit.getPlayer(mcrsPlayer.getUUID());

        if (bukkitPlayer == null) {
            LogUtil.severe("Player " + mcrsPlayer.getName() + " is null!");
            return;
        }

        bossBarManager.showXPBar(bukkitPlayer, type, skill.getExperience(), amount);

        if (leveledUp && newLevel > level) {
            bukkitPlayer.sendMessage(Component.text("Congratulations, you just advanced a " + type.getName() + " level.", NamedTextColor.BLUE));
            bukkitPlayer.sendMessage(Component.text("Your " + type.getName() + " level is now " + newLevel, NamedTextColor.YELLOW));
        }
    }
}