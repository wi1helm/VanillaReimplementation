package net.minestom.vanilla.commands;


import net.minestom.server.command.CommandManager;
import net.minestom.vanilla.commands.admin.OpCommand;
import net.minestom.vanilla.commands.all.HelpCommand;
import net.minestom.vanilla.commands.all.ListCommand;
import net.minestom.vanilla.commands.all.MeCommand;
import net.minestom.vanilla.commands.gamemaster.DifficultyCommand;
import net.minestom.vanilla.commands.gamemaster.ForceloadCommand;
import net.minestom.vanilla.commands.gamemaster.GamemodeCommand;
import net.minestom.vanilla.commands.gamemaster.SayCommand;
import net.minestom.vanilla.commands.owner.SaveAllCommand;
import net.minestom.vanilla.commands.owner.StopCommand;
import net.minestom.vanilla.logging.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class VanillaCommandsFeature {

    private static final Map<String, VanillaCommand> registeredCommands = new HashMap<>();


    private static final List<VanillaCommand> VANILLA_COMMANDS = List.of(
            new HelpCommand(),
            new ListCommand(),
            new MeCommand(),
            new DifficultyCommand(),
            new ForceloadCommand(),
            new GamemodeCommand(),
            new SayCommand(),
            new SaveAllCommand(),
            new StopCommand(),
            new OpCommand()
    );

    public static void registerCommand(CommandManager manager, VanillaCommand command) {

        manager.register(command);
        registeredCommands.put(command.getName(), command);

        if (!VANILLA_COMMANDS.contains(command)) {
            Logger.info("Registered custom vanilla command " +  command.getName());
        }
    }

    public static void registerAll(CommandManager manager) {
        VANILLA_COMMANDS.forEach(command -> registerCommand(manager, command));
        Logger.info("Registered " + VANILLA_COMMANDS.size() + " vanilla commands");
    }

    public static Map<String, VanillaCommand> getRegisteredCommands() {
        return registeredCommands;
    }

    public static List<VanillaCommand> getVanillaCommands() {
        return VANILLA_COMMANDS;
    }
}