package com.waldxn.MCRS.command;

import com.waldxn.MCRS.player.PlayerManager;
import com.waldxn.MCRS.skill.core.SkillType;
import com.waldxn.MCRS.skill.manager.DatabaseManager;
import com.waldxn.MCRS.ui.LeaderboardGui;
import com.waldxn.MCRS.util.ChatUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("NullableProblems")
public class LeaderboardCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!commandSender.hasPermission("mcrs.player.leaderboard")) {
            commandSender.sendMessage(ChatUtil.color("&4You do not have permission to use this command."));
            return true;
        }

        if (!(commandSender instanceof Player player)) {
            commandSender.sendMessage(ChatUtil.color("&4You must be a player to use this command."));
            return true;
        }

        if (!DatabaseManager.isConnected()) {
            commandSender.sendMessage(ChatUtil.color("&4You are not connected to the database."));
            return true;
        }

        if (strings.length > 1) return false;

        if (strings.length == 0) {
            LeaderboardGui.open(PlayerManager.get(player.getUniqueId()), null);
            return true;
        }

        String skillName = strings[0];

        SkillType skill;
        try {
            skill = SkillType.valueOf(skillName.toUpperCase());
        } catch (IllegalArgumentException e) {
            commandSender.sendMessage("Invalid skill name");
            return true;
        }

        LeaderboardGui.open(PlayerManager.get(player.getUniqueId()), skill);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 1) {
            return Arrays.stream(SkillType.values())
                    .map(skill -> skill.name().toLowerCase())
                    .filter(name -> name.startsWith(strings[0].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
