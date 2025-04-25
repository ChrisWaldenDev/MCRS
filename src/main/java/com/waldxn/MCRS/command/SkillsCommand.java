package com.waldxn.MCRS.command;

import com.waldxn.MCRS.player.MCRSPlayer;
import com.waldxn.MCRS.player.PlayerManager;
import com.waldxn.MCRS.ui.StatsGui;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SkillsCommand implements CommandExecutor {

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage("This command can only be executed by a player");
            return true;
        }

        if (strings.length > 1) return false;

        if (strings.length == 0) {
            StatsGui.open(PlayerManager.get(player.getUniqueId()));
            return true;
        }

        OfflinePlayer bukkitPlayer = Bukkit.getOfflinePlayer(strings[0]);

        if (!bukkitPlayer.hasPlayedBefore()) {
            commandSender.sendMessage("Player not found");
            return true;
        }

        UUID uuid = bukkitPlayer.getUniqueId();
        MCRSPlayer target = PlayerManager.getOrLoad(uuid);
        MCRSPlayer viewer = PlayerManager.get(player.getUniqueId());

        StatsGui.open(viewer, target);
        return true;
    }
}
