package com.waldxn.MCRS.ui;

import com.waldxn.MCRS.player.MCRSPlayer;
import com.waldxn.MCRS.skill.core.Skill;
import com.waldxn.MCRS.skill.core.SkillType;
import com.waldxn.MCRS.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class StatsGui {

    public static void open(MCRSPlayer MCRSPlayer) {

        int totalLevel = 0;
        for (Skill s: MCRSPlayer.getSkills().values()) {
            totalLevel += s.getLevel();
        }

        Inventory gui = Bukkit.createInventory(null, 27, ChatUtil.color("&l" + MCRSPlayer.getName() + "'s Total Level: &2&l" + totalLevel));

        int slot = 0;
        for (SkillType skillType : SkillType.values()) {
            while ((slot % 9 < 3 || slot % 9 > 5) && slot >= 18) slot++;

            ItemStack item = new ItemStack(skillType.getIcon());
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(skillType.getName());
            meta.setLore(MCRSPlayer.getSkill(skillType).getHoverInfo());
            item.setItemMeta(meta);
            gui.setItem(slot, item);
            slot++;
        }
        MCRSPlayer.getBukkitPlayer().openInventory(gui);
    }
}
