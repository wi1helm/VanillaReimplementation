package net.minestom.vanilla.commands;

import net.minestom.server.command.CommandManager;
import net.minestom.server.command.builder.Command;
import net.minestom.vanilla.commands.all.HelpCommand;
import net.minestom.vanilla.commands.all.ListCommand;
import net.minestom.vanilla.commands.all.MeCommand;
import net.minestom.vanilla.commands.gamemaster.*;
import net.minestom.vanilla.commands.owner.SaveAllCommand;
import net.minestom.vanilla.commands.owner.StopCommand;
import net.minestom.vanilla.logging.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * All commands available in the vanilla reimplementation
 */
public final class VanillaCommandsFeature {

    private static final Map<String, Supplier<VanillaCommand>> registeredCommands = new HashMap<>();

    private static final List<Supplier<VanillaCommand>> COMMANDS = List.of(
            OpCommand::new,
            DifficultyCommand::new,
            GamemodeCommand::new,
            HelpCommand::new,
            StopCommand::new,
            MeCommand::new,
            SaveAllCommand::new,
            ForceloadCommand::new,
            ListCommand::new,
            SayCommand::new
    );

    public static void registerAll(@NotNull CommandManager manager) {
        for (Supplier<VanillaCommand> commandSupplier : COMMANDS) {
            Command command = commandSupplier.get();
            registeredCommands.put(command.getName(), commandSupplier);
            manager.register(command);
        }
        Logger.info("Registered {} vanilla commands", COMMANDS.size());
    }

    public static List<String> getRegisteredCommandNames() {
        return COMMANDS.stream()
                .map(Supplier::get)
                .flatMap(command -> Arrays.stream(command.getNames()))
                .distinct()
                .toList();
    }

    public static Map<String, VanillaCommand> getRegisteredCommands() {
        Map<String, VanillaCommand> map = new HashMap<>();

        for (Supplier<VanillaCommand> supplier : COMMANDS) {
            Command command = supplier.get();

            if (command instanceof VanillaCommand vanillaCommand) {
                for (String name : command.getNames()) {
                    map.put(name.toLowerCase(), vanillaCommand);
                }
            }
        }

        return map;
    }

    public static VanillaCommand getCommandFromName(String name) {
        return registeredCommands.get(name).get();
    }


}
