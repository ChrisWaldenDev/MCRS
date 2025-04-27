package com.waldxn.MCRS.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.waldxn.MCRS.common.util.Permissions;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;

@SuppressWarnings("UnstableApiUsage")
public class HelpCommand implements Subcommand{
    @Override
    public LiteralArgumentBuilder<CommandSourceStack> build() {

        return Commands.literal("help")
                .requires(sender ->
                        sender.getSender().hasPermission(Permissions.PLAYER_HELP)
                        || sender.getSender().hasPermission(Permissions.ADMIN_HELP))
                .executes(this::showHelp);
    }

    private int showHelp(CommandContext<CommandSourceStack> context) {
        CommandSender sender = context.getSource().getSender();
        sender.sendMessage(Component.text("========== [MCRS Commands] ==========", NamedTextColor.GOLD));

        if (sender.hasPermission(Permissions.ADMIN_HELP)) {
            // Admin commands
            sender.sendMessage(Component.text("/mcrs saveall [force]", NamedTextColor.YELLOW).append(Component.text(" - Save all player skills that have changed (Force saves all players)", NamedTextColor.GRAY)));
            sender.sendMessage(Component.text("/mcrs setskill <player> <skill> <level>", NamedTextColor.YELLOW).append(Component.text(" - Set a player's skill level", NamedTextColor.GRAY)));
            sender.sendMessage(Component.text("/mcrs wipeskills <player>", NamedTextColor.YELLOW).append(Component.text(" - Wipe all skills for a player", NamedTextColor.GRAY)));
        }

        // Player commands
        sender.sendMessage(Component.text("/mcrs skills [player]", NamedTextColor.GREEN).append(Component.text(" - View your or another player's skills", NamedTextColor.GRAY)));
        sender.sendMessage(Component.text("/mcrs leaderboard [skill]", NamedTextColor.GREEN).append(Component.text(" - View the skill leaderboards", NamedTextColor.GRAY)));
        sender.sendMessage(Component.text("===================================", NamedTextColor.GOLD));
        return 1;
    }
}
