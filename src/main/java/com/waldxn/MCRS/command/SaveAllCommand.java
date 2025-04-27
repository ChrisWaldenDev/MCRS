package com.waldxn.MCRS.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.waldxn.MCRS.MCRS;
import com.waldxn.MCRS.common.util.LogUtil;
import com.waldxn.MCRS.common.util.Permissions;
import com.waldxn.MCRS.player.MCRSPlayer;
import com.waldxn.MCRS.player.PlayerDataDAO;
import com.waldxn.MCRS.player.PlayerManager;
import com.waldxn.MCRS.skill.manager.DatabaseManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;

@SuppressWarnings("UnstableApiUsage")
public class SaveAllCommand implements Subcommand {

    private final DatabaseManager databaseManager = MCRS.getServiceRegistry().getDatabaseManager();
    private final PlayerManager playerManager = MCRS.getServiceRegistry().getPlayerManager();
    private final PlayerDataDAO playerDataDAO = MCRS.getServiceRegistry().getPlayerDataDAO();

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> build() {
        return Commands.literal("saveall")
                .requires(sender -> sender.getSender().hasPermission(Permissions.ADMIN_SAVE_ALL))
                .executes(context -> {
                    savePlayerSkills(context, false);
                    return 1;
                })
                .then(
                        Commands.argument("force", StringArgumentType.word())
                                .suggests((context, builder) -> {
                                    builder.suggest("force");
                                    return builder.buildFuture();
                                })
                                .executes(context -> {
                                    String force = StringArgumentType.getString(context, "force");

                                    if (force.equalsIgnoreCase("force")) {
                                        savePlayerSkills(context, true);
                                        return 1;
                                    } else {
                                        context.getSource().getSender().sendMessage(
                                                Component.text("Invalid argument. Use 'force' to save all players.", NamedTextColor.DARK_RED)
                                        );
                                    }
                                    return 0;
                                })
                );
    }

    private void savePlayerSkills(CommandContext<CommandSourceStack> context, boolean force) {
        CommandSender commandSender = context.getSource().getSender();
        if (!databaseManager.isConnected()) {
            commandSender.sendMessage(Component.text("You are not connected to the database.", NamedTextColor.DARK_RED));
            return;
        }

        for (MCRSPlayer player : playerManager.getPlayers()) {
            if (force || player.isDirty()) playerDataDAO.savePlayerSkills(player);
        }

        if (force) {
            commandSender.sendMessage(Component.text("All players have been saved. (Forced)", NamedTextColor.GREEN));
            LogUtil.info("/saveall triggered - all player data saved. (forced)");
            return;
        }

        commandSender.sendMessage(Component.text("All players have been saved.", NamedTextColor.GREEN));
        LogUtil.info("/saveall triggered - all player data saved.");
    }
}
