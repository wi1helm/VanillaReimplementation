package net.minestom.vanilla.commands;

import net.minestom.server.command.CommandManager;
import net.minestom.server.command.builder.Command;
import net.minestom.vanilla.commands.all.HelpCommand;
import net.minestom.vanilla.commands.all.ListCommand;
import net.minestom.vanilla.commands.all.MeCommand;
import net.minestom.vanilla.commands.gamemaster.*;
import net.minestom.vanilla.commands.owner.SaveAllCommand;
import net.minestom.vanilla.commands.owner.StopCommand;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

/**
 * All commands available in the vanilla reimplementation
 */
public enum VanillaCommandsFeature {

    OP(OpCommand::new),
    DIFFICULTY(DifficultyCommand::new),
    GAMEMODE(GamemodeCommand::new),
    HELP(HelpCommand::new),
    STOP(StopCommand::new),
    ME(MeCommand::new),
    SAVEALL(SaveAllCommand::new),
    FORCELOAD(ForceloadCommand::new),
    LIST(ListCommand::new),
    SAY(SayCommand::new)
    ;

    private static final Logger log = LoggerFactory.getLogger(VanillaCommandsFeature.class);
    private final Supplier<Command> commandCreator;

    VanillaCommandsFeature(Supplier<Command> commandCreator) {
        this.commandCreator = commandCreator;
    }

    /**
     * Register all vanilla commands into the given manager
     *
     * @param manager the command manager to register commands on
     */
    public static void registerAll(@NotNull CommandManager manager) {
        for (VanillaCommandsFeature vanillaCommand : values()) {
            Command command = vanillaCommand.commandCreator.get();
            manager.register(command);
        }
        log.info("Registered {} vanilla commands", VanillaCommandsFeature.values().length);
    }

    public @Nullable VanillaCommand getCommand() {
        if (this.commandCreator.get() instanceof VanillaCommand vc) return vc;
        return null;
    }
}
