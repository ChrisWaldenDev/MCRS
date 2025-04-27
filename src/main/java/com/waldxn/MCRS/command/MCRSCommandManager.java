package com.waldxn.MCRS.command;

import com.waldxn.MCRS.MCRS;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;

import java.util.List;


@SuppressWarnings("UnstableApiUsage")
public class MCRSCommandManager {

    public static void registerCommands(MCRS plugin) {
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            var root = Commands.literal("mcrs");

            List<Subcommand> subcommands = List.of(
                    new HelpCommand(),
                    new SaveAllCommand(),
                    new SetSkillCommand(),
                    new SkillsCommand(),
                    new LeaderboardCommand(),
                    new WipeSkillsCommand()
            );

            for (Subcommand subcommand : subcommands) {
                root.then(subcommand.build());

            }

            commands.registrar().register(root.build());
        });
    }
}
