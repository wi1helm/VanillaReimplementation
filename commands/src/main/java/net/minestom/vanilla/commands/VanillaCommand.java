package net.minestom.vanilla.commands;

import net.kyori.adventure.text.Component;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.condition.CommandCondition;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class VanillaCommand extends Command {

    public final int LEVEL_ALL = 0;
    public final int LEVEL_MODERATOR = 1;
    public final int LEVEL_GAMEMASTER = 2;
    public final int LEVEL_ADMIN = 3;
    public final int LEVEL_OWNER = 4;

    public VanillaCommand(@NotNull String name, @Nullable String... aliases) {
        super(name, aliases);
    }

    public VanillaCommand(@NotNull String name) {
        super(name);
    }

    public abstract Component usage(CommandSender sender, CommandContext context);

    protected abstract void defaultor(CommandSender sender, CommandContext context);

    public CommandCondition permission(int level) {
        return (sender, commandName) -> {
            if (sender instanceof ConsoleSender) return true;
            if (sender instanceof Player player) return player.getPermissionLevel() >= level;
            return false;
        };
    }

    public boolean hasNoArguments(CommandContext context) {
        return context.getCommandName().equals(context.getInput());
    }

}
