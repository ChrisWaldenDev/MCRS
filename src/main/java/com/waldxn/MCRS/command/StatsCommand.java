package com.waldxn.MCRS.command;

import com.waldxn.MCRS.player.PlayerManager;
import com.waldxn.MCRS.ui.StatsGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatsCommand implements CommandExecutor {

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (commandSender instanceof Player player) {
            StatsGui.open(PlayerManager.get(player.getUniqueId()));
            return true;
        }
        commandSender.sendMessage("This command can only be executed by a player");
        return false;
    }
}
