package net.minestom.vanilla.commands.all;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.ArgumentWord;
import net.minestom.vanilla.commands.VanillaCommand;
import net.minestom.vanilla.commands.VanillaCommandsFeature;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * Returns the list of all available commands
 */
public class HelpCommand extends VanillaCommand {
    public HelpCommand() {
        super("help");

        setCondition(permission(LEVEL_ALL));

        setDefaultExecutor(this::defaultor);
        ArgumentWord command = ArgumentType.Word("command").from(VanillaCommandsFeature.getRegisteredCommandNames().toArray(String[]::new)); // sets autocomplete suggestions

        addSyntax((sender, context) -> {
            String vanillaCommandName = context.get(command);
            
            viewUsage(sender, context, VanillaCommandsFeature.getCommandFromName(vanillaCommandName));

        }, command);

    }

    private void viewUsage(@NotNull CommandSender sender, CommandContext context, @Nullable VanillaCommand command) {

        if (command == null) {
            return;
        }

        Component usage = command.usage(sender, context);

        if (usage == null) {
            sender.sendMessage(Component.text("Usage not found!").color(NamedTextColor.RED));
            return;
        }
        sender.sendMessage(usage);
    }

    private int compareCommands(VanillaCommand a, VanillaCommand b) {
        return a.getName().compareTo(b.getName());
    }

    public Component usage(CommandSender sender, CommandContext context) {
        return Component.text("/help [<command>]");
    }

    public void defaultor(CommandSender sender, CommandContext context) {

        if (hasNoArguments(context)) {

            sender.sendMessage("=== Help ===");

            List<VanillaCommand> commands = new ArrayList<>(VanillaCommandsFeature.getRegisteredCommands().values());

            commands.sort(this::compareCommands);

            commands.forEach(command -> sender.sendMessage("/" + command.getName().toLowerCase()));

            sender.sendMessage("============");

            return;
        }

        sender.sendMessage(Component.text("Unknown command: ").color(NamedTextColor.RED)
                .append(Component.text(context.getInput().replace(context.getCommandName() + " ","")).color(NamedTextColor.RED).decoration(TextDecoration.UNDERLINED, true)));


    }
}
