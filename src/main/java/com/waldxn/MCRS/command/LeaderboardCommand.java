package com.waldxn.MCRS.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.waldxn.MCRS.player.PlayerManager;
import com.waldxn.MCRS.skill.core.SkillType;
import com.waldxn.MCRS.skill.manager.DatabaseManager;
import com.waldxn.MCRS.ui.LeaderboardGui;
import com.waldxn.MCRS.util.Permissions;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


@SuppressWarnings("UnstableApiUsage")
public class LeaderboardCommand implements Subcommand {

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> build() {

        return Commands.literal("leaderboard")
                .requires(sender -> sender.getSender().hasPermission(Permissions.PLAYER_LEADERBOARD))
                .executes(context -> openLeaderboard(context, null))
                .then(
                        Commands.argument("skill", StringArgumentType.word())
                                .suggests((context, builder) -> {
                                    String input = builder.getRemainingLowerCase();
                                    for (SkillType skill : SkillType.values()) {
                                        if (skill.name().toLowerCase().startsWith(input))
                                            builder.suggest(skill.name());
                                    }
                                    return builder.buildFuture();
                                })
                                .executes(context -> {
                                    String skillName = StringArgumentType.getString(context, "skill");
                                    SkillType skill;
                                    try {
                                        skill = SkillType.valueOf(skillName.toUpperCase());
                                    } catch (IllegalArgumentException e) {
                                        CommandSender sender = context.getSource().getSender();
                                        sender.sendMessage(Component.text("Invalid skill name", NamedTextColor.DARK_RED));
                                        return 0;
                                    }

                                    openLeaderboard(context, skill);
                                    return 1;
                                })
                );
    }

    private int openLeaderboard(CommandContext<CommandSourceStack> context, SkillType skillType) {
        final CommandSender sender = context.getSource().getSender();
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("You must be a player to use this command.", NamedTextColor.DARK_RED));
            return 0;
        }

        if (!DatabaseManager.isConnected()) {
            sender.sendMessage(Component.text("You are not connected to the database.", NamedTextColor.DARK_RED));
            return 0;
        }

        LeaderboardGui.open(PlayerManager.get(player.getUniqueId()), skillType);
        return 1;
    }
}
