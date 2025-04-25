package com.waldxn.MCRS.command;

import com.waldxn.MCRS.player.MCRSPlayer;
import com.waldxn.MCRS.player.PlayerDataDAO;
import com.waldxn.MCRS.player.PlayerManager;
import com.waldxn.MCRS.skill.manager.DatabaseManager;
import com.waldxn.MCRS.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("NullableProblems")
public class WipeSkillsCommand implements CommandExecutor {

    private final List<MCRSPlayer> confirmPlayer = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!commandSender.hasPermission("mcrs.admin.wipestats")) {
            commandSender.sendMessage(ChatUtil.color("&4You do not have permission to use this command."));
            return true;
        }

        if (!DatabaseManager.isConnected()) {
            commandSender.sendMessage(ChatUtil.color("&4You are not connected to the database."));
            return true;
        }

        if (strings.length != 1) return false;

        OfflinePlayer bukkitPlayer = Bukkit.getOfflinePlayer(strings[0]);

        if (!bukkitPlayer.hasPlayedBefore()) {
            commandSender.sendMessage("Player not found");
            return true;
        }

        UUID uuid = bukkitPlayer.getUniqueId();

        MCRSPlayer player = PlayerManager.getOrLoad(uuid);

        if (confirmPlayer.contains(player)) {
            commandSender.sendMessage(ChatUtil.color("&aWiping stats for &e" + player.getName()));
            player.initializeSkills();
            PlayerDataDAO.savePlayerSkills(player);
            confirmPlayer.remove(player);
        } else {
            confirmPlayer.add(player);
            commandSender.sendMessage(ChatUtil.color("&cAre you sure you want to wipe the stats of &e" + player.getName() + "&c?"));
            commandSender.sendMessage(ChatUtil.color("&cType &e/wipestats " + player.getName() + " &cagain to confirm."));
        }
        return true;
    }
}
