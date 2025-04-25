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

    public static void open(MCRSPlayer viewer, MCRSPlayer target) {

        int totalLevel = calculateTotalLevel(target);

        Inventory gui = Bukkit.createInventory(null, 27, ChatUtil.color("&l" + target.getName() + "'s Total Level: &2&l" + totalLevel));

        int slot = 0;
        for (SkillType type : SkillType.values()) {
            // Centers the bottom row of skill icons
            while ((slot % 9 < 3 || slot % 9 > 5) && slot >= 18) slot++;

            ItemStack item = createSkillItem(type, target.getSkill(type));
            gui.setItem(slot, item);
            slot++;
        }
        viewer.getBukkitPlayer().openInventory(gui);
    }

    public static void open(MCRSPlayer player) {
        open(player, player);
    }

    private static int calculateTotalLevel(MCRSPlayer player) {
        int totalLevel = 0;
        for (Skill skill : player.getSkills().values()) {
            totalLevel += skill.getLevel();
        }
        return totalLevel;
    }

    private static ItemStack createSkillItem(SkillType type, Skill skill) {
        ItemStack item = new ItemStack(type.getIcon());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(type.getName());
        meta.setLore(skill.getHoverInfo());
        item.setItemMeta(meta);
        return item;
    }
}