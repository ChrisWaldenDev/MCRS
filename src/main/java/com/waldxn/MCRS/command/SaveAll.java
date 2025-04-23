package com.waldxn.MCRS.command;

import com.waldxn.MCRS.player.MCRSPlayer;
import com.waldxn.MCRS.player.PlayerDataDAO;
import com.waldxn.MCRS.player.PlayerManager;
import com.waldxn.MCRS.skill.manager.DatabaseManager;
import com.waldxn.MCRS.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SaveAll implements CommandExecutor {

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (!commandSender.hasPermission("mcrs.admin.saveall")) {
            commandSender.sendMessage(ChatUtil.color("&4You do not have permission to use this command."));
            return true;
        }

        if (!DatabaseManager.isConnected()) {
            commandSender.sendMessage(ChatUtil.color("&4You are not connected to the database."));
            return true;
        }

        boolean force = strings.length > 0 && strings[0].equalsIgnoreCase("--force");

        for (MCRSPlayer player : PlayerManager.getPlayers()) {
            if (force || player.isDirty()) PlayerDataDAO.savePlayerSkills(player);
        }

        commandSender.sendMessage(ChatUtil.color("&aAll players have been saved."));
        Bukkit.getLogger().info("/saveall triggered - all player data saved.");

        return true;
    }
}
