package com.waldxn.MCRS.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.waldxn.MCRS.player.MCRSPlayer;
import com.waldxn.MCRS.player.PlayerManager;
import com.waldxn.MCRS.ui.SkillsGui;
import com.waldxn.MCRS.util.Permissions;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("UnstableApiUsage")
public class SkillsCommand implements Subcommand {

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> build() {
        return Commands.literal("skills")
                .requires(sender -> sender.getSender().hasPermission(Permissions.PLAYER_SKILLS))
                .executes(this::showSkills)
                .then(
                        Commands.argument("player", ArgumentTypes.player())
                                .suggests((context, builder) -> {
                                    for (MCRSPlayer player : PlayerManager.getPlayers()) {
                                        String name = player.getName();
                                        if (name.toLowerCase().startsWith(builder.getRemainingLowerCase()))
                                            builder.suggest(name);
                                    }
                                    return builder.buildFuture();
                                })
                                .executes(this::showSkills)
                );
    }

    private int showSkills(CommandContext<CommandSourceStack> context) {
        final CommandSender sender = context.getSource().getSender();
        if (!(sender instanceof Player viewer)) {
            sender.sendMessage("You must be a player to use this command.");
            return 0;
        }

        MCRSPlayer target;

        if (context.getNodes().stream().anyMatch(node -> node.getNode().getName().equals("player"))) {
            // Player argument is present
            try {
                PlayerSelectorArgumentResolver resolver = context.getArgument("player", PlayerSelectorArgumentResolver.class);
                Player resolvedPlayer = resolver.resolve(context.getSource()).getFirst();
                if (resolvedPlayer == null) {
                    viewer.sendMessage(Component.text("Player not found", NamedTextColor.DARK_RED));
                    return 0;
                }

                target = PlayerManager.getOrLoad(resolvedPlayer.getUniqueId());
            } catch (CommandSyntaxException e) {
                viewer.sendMessage(Component.text("Invalid player selector", NamedTextColor.DARK_RED));
                return 0;
            }
        } else {
            target = PlayerManager.get(viewer.getUniqueId());
        }

        SkillsGui.open(PlayerManager.get(viewer.getUniqueId()), target);
        return 1;

    }
}
