package com.waldxn.MCRS.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.waldxn.MCRS.cache.LeaderboardCache;
import com.waldxn.MCRS.player.MCRSPlayer;
import com.waldxn.MCRS.player.PlayerDataDAO;
import com.waldxn.MCRS.player.PlayerManager;
import com.waldxn.MCRS.skill.core.SkillType;
import com.waldxn.MCRS.skill.manager.DatabaseManager;
import com.waldxn.MCRS.util.Permissions;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

@SuppressWarnings("UnstableApiUsage")
public class SetSkillCommand implements Subcommand {

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> build() {
        return Commands.literal("setskill")
                .requires(sender -> sender.getSender().hasPermission(Permissions.ADMIN_SET_SKILL))
                .then(
                        Commands.argument("player", ArgumentTypes.player())
                                .suggests((context, builder) -> {
                                    for (Map.Entry<MCRSPlayer, Double> entry : LeaderboardCache.getTotalXPLeaderboard()) {
                                        MCRSPlayer player = entry.getKey();
                                        String name = player.getName();
                                        if (name.toLowerCase().startsWith(builder.getRemainingLowerCase()))
                                            builder.suggest(name);
                                    }
                                    return builder.buildFuture();
                                })

                                .then(
                                        Commands.argument("skill", StringArgumentType.word())
                                                .suggests((context, builder) -> {
                                                    String input = builder.getRemainingLowerCase();
                                                    for (SkillType skill : SkillType.values()) {
                                                        if (skill.name().toLowerCase().startsWith(input))
                                                            builder.suggest(skill.name().toLowerCase());
                                                    }
                                                    return builder.buildFuture();
                                                })

                                                .then(
                                                        Commands.argument("value", IntegerArgumentType.integer(1, 99))
                                                                .executes(context -> {
                                                                    final PlayerSelectorArgumentResolver targetResolver = context.getArgument("player", PlayerSelectorArgumentResolver.class);
                                                                    final Player player = targetResolver.resolve(context.getSource()).getFirst();
                                                                    String skillName = StringArgumentType.getString(context, "skill");
                                                                    int newValue = IntegerArgumentType.getInteger(context, "value");

                                                                    return setSkill(context, player.getName(), skillName, newValue);
                                                                })
                                                )));
    }

    private int setSkill(CommandContext<CommandSourceStack> context, String playerName, String skillName, int newValue) {
        CommandSender commandSender = context.getSource().getSender();
        if (!DatabaseManager.isConnected()) {
            commandSender.sendMessage(Component.text("You are not connected to the database.", NamedTextColor.DARK_RED));
            return 0;
        }

        OfflinePlayer bukkitPlayer = Bukkit.getOfflinePlayer(playerName);

        if (!bukkitPlayer.hasPlayedBefore()) {
            commandSender.sendMessage("Player not found");
            return 0;
        }

        UUID uuid = bukkitPlayer.getUniqueId();

        SkillType skill;
        try {
            skill = SkillType.valueOf(skillName.toUpperCase());
        } catch (IllegalArgumentException e) {
            commandSender.sendMessage("Invalid skill name");
            return 0;
        }

        MCRSPlayer player = PlayerManager.getOrLoad(uuid);
        player.setSkillLevel(skill, newValue);
        PlayerDataDAO.savePlayerSkills(player);

        commandSender.sendMessage(Component.text("Set " + player.getName() + "'s " + skill.getName() + " level to " + newValue, NamedTextColor.GREEN));

        return 1;
    }
}
