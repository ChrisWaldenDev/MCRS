package com.waldxn.MCRS.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;

@SuppressWarnings("UnstableApiUsage")
public interface Subcommand {
    LiteralArgumentBuilder<CommandSourceStack> build();
}
