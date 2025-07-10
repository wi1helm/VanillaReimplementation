package net.minestom.vanilla.commands;

import net.kyori.adventure.text.Component;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.condition.CommandCondition;
import net.minestom.server.entity.Player;

public interface VanillaCommand {



    Component usage(CommandSender sender, CommandContext context);

    void defaultor(CommandSender sender, CommandContext context);


    int LEVEL_ALL = 0;
    int LEVEL_MODERATOR = 1;
    int LEVEL_GAMEMASTER = 2;
    int LEVEL_ADMIN = 3;
    int LEVEL_OWNER = 4;

    default CommandCondition permission(int level) {
        return (sender, commandName) -> {
            if (sender instanceof ConsoleSender) return true;
            if (sender instanceof Player player) return player.getPermissionLevel() >= level;
            return false;
        };
    }

    default boolean hasArguments(CommandContext context) {
        return !context.getCommandName().equals(context.getInput());
    }
}
