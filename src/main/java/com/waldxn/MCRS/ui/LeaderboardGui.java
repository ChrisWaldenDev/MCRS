package com.waldxn.MCRS.ui;

import com.waldxn.MCRS.cache.LeaderboardCache;
import com.waldxn.MCRS.player.MCRSPlayer;
import com.waldxn.MCRS.skill.core.Skill;
import com.waldxn.MCRS.skill.core.SkillType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

public class LeaderboardGui {

    public static void open(MCRSPlayer player, @Nullable SkillType skillType) {

        List<Map.Entry<MCRSPlayer, Double>> leaderboard = (skillType == null) ?
                LeaderboardCache.getTotalXPLeaderboard() :
                LeaderboardCache.getLeaderboard(skillType);

        int maxEntries = 25;
        int totalSlots = 27;

        Component title = (skillType == null)
                ? Component.text("Total Level Leaderboard", NamedTextColor.DARK_GRAY, TextDecoration.BOLD)
                : Component.text(skillType.getName() + " Leaderboard", NamedTextColor.DARK_GRAY, TextDecoration.BOLD);

        Inventory gui = Bukkit.createInventory(null, 27, title);

        // Adds the top players to the leaderboard
        for (int i = 0; i < Math.min(maxEntries, leaderboard.size()); i++) {
            Map.Entry<MCRSPlayer, Double> entry = leaderboard.get(i);
            gui.setItem(i, createLeaderboardItem(entry.getKey(), i + 1, skillType));
        }

        if (leaderboard.size() < totalSlots) {
            gui.setItem(totalSlots - 1, createLeaderboardItem(player, 0, skillType));
        }
        player.getBukkitPlayer().openInventory(gui);
    }

    private static ItemStack createLeaderboardItem(MCRSPlayer player, int rank, @Nullable SkillType skillType) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();

        String rankDisplay = rank > 0 ? "#" + rank + " " : "";
        meta.displayName(Component.text(rankDisplay, NamedTextColor.WHITE)
                .append(Component.text(player.getName(), NamedTextColor.GREEN)));

        List<Component> lore = new java.util.ArrayList<>();

        DecimalFormat formatter = new DecimalFormat("#,###");
        String currentXP = formatter.format((int) (skillType != null
                ? player.getSkill(skillType).getExperience()
                : getTotalXP(player)));

        lore.add(Component.text("Level " + (skillType != null
                ? player.getSkill(skillType).getLevel()
                : getTotalLevel(player)), NamedTextColor.GOLD));

        lore.add(Component.text("XP: " + currentXP, NamedTextColor.YELLOW));

        meta.lore(lore);
        meta.setOwningPlayer(Bukkit.getOfflinePlayer(player.getUUID()));
        skull.setItemMeta(meta);
        return skull;
    }

    private static int getTotalLevel(MCRSPlayer p) {
        return p.getSkills().values().stream()
                .mapToInt(Skill::getLevel)
                .sum();
    }

    private static double getTotalXP(MCRSPlayer p) {
        return p.getSkills().values().stream()
                .mapToDouble(Skill::getExperience)
                .sum();
    }
}
