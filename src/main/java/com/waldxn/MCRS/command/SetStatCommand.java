package com.waldxn.MCRS.command;

import com.waldxn.MCRS.player.MCRSPlayer;
import com.waldxn.MCRS.player.PlayerDataDAO;
import com.waldxn.MCRS.player.PlayerManager;
import com.waldxn.MCRS.skill.core.SkillType;
import com.waldxn.MCRS.skill.manager.DatabaseManager;
import com.waldxn.MCRS.util.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@SuppressWarnings("NullableProblems")
public class SetStatCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!commandSender.hasPermission("mcrs.admin.setstat")) {
            commandSender.sendMessage(ChatUtil.color("&4You do not have permission to use this command."));
            return true;
        }

        if (!DatabaseManager.isConnected()) {
            commandSender.sendMessage(ChatUtil.color("&4You are not connected to the database."));
            return true;
        }

        if (strings.length != 3) return false;

        String playerName = strings[0];
        String skillName = strings[1];
        String value = strings[2];

        OfflinePlayer bukkitPlayer = Bukkit.getOfflinePlayer(strings[0]);

        if (!bukkitPlayer.hasPlayedBefore()) {
            commandSender.sendMessage("Player not found");
            return true;
        }

        UUID uuid = bukkitPlayer.getUniqueId();

        SkillType skill;
        try {
            skill = SkillType.valueOf(skillName.toUpperCase());
        } catch (IllegalArgumentException e) {
            commandSender.sendMessage("Invalid skill name");
            return true;
        }

        int newValue;
        try {
            newValue = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            commandSender.sendMessage("Invalid value. Must be an integer between 1-99");
            return true;
        }

        MCRSPlayer player = PlayerManager.getOrLoad(uuid);
        player.setSkillLevel(skill, newValue);
        PlayerDataDAO.savePlayerSkills(player);

        commandSender.sendMessage(ChatUtil.color("&aSet " + playerName + "'s " + skillName + " level to " + newValue));

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 2) {
            return Arrays.stream(SkillType.values())
                    .map(skill -> skill.name().toLowerCase())
                    .filter(name -> name.startsWith(strings[1].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
