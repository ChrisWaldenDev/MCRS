package com.waldxn.MCRS.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.waldxn.MCRS.MCRS;
import com.waldxn.MCRS.common.cache.LeaderboardCache;
import com.waldxn.MCRS.common.util.Permissions;
import com.waldxn.MCRS.player.MCRSPlayer;
import com.waldxn.MCRS.player.PlayerDataDAO;
import com.waldxn.MCRS.player.PlayerManager;
import com.waldxn.MCRS.skill.manager.DatabaseManager;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@SuppressWarnings("UnstableApiUsage")
public class WipeSkillsCommand implements Subcommand {

    private final List<UUID> confirmPlayer = new ArrayList<>();
    private final DatabaseManager databaseManager = MCRS.getServiceRegistry().getDatabaseManager();
    private final PlayerManager playerManager = MCRS.getServiceRegistry().getPlayerManager();
    private final PlayerDataDAO playerDataDAO = MCRS.getServiceRegistry().getPlayerDataDAO();
    private final LeaderboardCache leaderboardCache = MCRS.getServiceRegistry().getLeaderboardCache();

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> build() {
        return Commands.literal("wipeskills")
                .requires(sender -> sender.getSender().hasPermission(Permissions.ADMIN_WIPE_SKILLS))
                .then(Commands.argument("player", ArgumentTypes.player())
                        .suggests((context, builder) -> {
                            for (Map.Entry<MCRSPlayer, Double> entry : leaderboardCache.getTotalXPLeaderboard()) {
                                MCRSPlayer player = entry.getKey();
                                String name = player.getName();
                                if (name.toLowerCase().startsWith(builder.getRemainingLowerCase()))
                                    builder.suggest(name);
                            }
                            return builder.buildFuture();
                        })
                        .executes(context -> {
                            PlayerSelectorArgumentResolver targetResolver = context.getArgument("player", PlayerSelectorArgumentResolver.class);
                            final Player player = targetResolver.resolve(context.getSource()).getFirst();

                            return wipeSkills(context, player.getName());
                        }));

    }


    private int wipeSkills(CommandContext<CommandSourceStack> context, String playerName) {
        CommandSender commandSender = context.getSource().getSender();
        if (!databaseManager.isConnected()) {
            commandSender.sendMessage(Component.text("You are not connected to the database.", NamedTextColor.DARK_RED));
            return 0;
        }

        OfflinePlayer bukkitPlayer = Bukkit.getOfflinePlayer(playerName);

        if (!bukkitPlayer.hasPlayedBefore()) {
            commandSender.sendMessage(Component.text("Player not found", NamedTextColor.DARK_RED));
            return 0;
        }

        UUID uuid = bukkitPlayer.getUniqueId();

        MCRSPlayer player = playerManager.getOrLoad(uuid);

        if (confirmPlayer.contains(player.getUUID())) {
            commandSender.sendMessage(Component.text("Wiping skills for ", NamedTextColor.GREEN)
                    .append(Component.text(player.getName(), NamedTextColor.YELLOW)));
            player.initializeSkills();
            playerDataDAO.savePlayerSkills(player);
            confirmPlayer.remove(player.getUUID());
        } else {
            confirmPlayer.add(player.getUUID());
            commandSender.sendMessage(Component.text("Are you sure you want to wipe the skills of ", NamedTextColor.DARK_RED)
                    .append(Component.text(player.getName(), NamedTextColor.YELLOW))
                    .append(Component.text("?", NamedTextColor.DARK_RED)));
            commandSender.sendMessage(Component.text("Type ", NamedTextColor.DARK_RED)
                    .append(Component.text("/wipeskills " + player.getName(), NamedTextColor.YELLOW))
                    .append(Component.text(" again to confirm.", NamedTextColor.DARK_RED)));
        }
        return 1;
    }
}