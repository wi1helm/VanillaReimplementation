package net.minestom.vanilla.commands.all;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.vanilla.commands.VanillaCommand;
import net.minestom.vanilla.commands.VanillaCommandsFeature;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Returns the list of all available commands
 */
public class HelpCommand extends Command implements VanillaCommand {
    public HelpCommand() {
        super("help");

        setCondition(permission(LEVEL_ALL));

        setDefaultExecutor(this::defaultor);

        ArgumentEnum<VanillaCommandsFeature> command = ArgumentType.Enum("command", VanillaCommandsFeature.class).setFormat(ArgumentEnum.Format.LOWER_CASED);

        addSyntax((sender, context) -> {
            VanillaCommandsFeature vanillaCommandsFeature = context.get(command);

            viewUsage(sender, context, vanillaCommandsFeature.getCommand());

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

    private int compareCommands(VanillaCommandsFeature a, VanillaCommandsFeature b) {
        return a.name().compareTo(b.name());
    }

    @Override
    public Component usage(CommandSender sender, CommandContext context) {
        return Component.text("/help [<command>]");
    }

    @Override
    public void defaultor(CommandSender sender, CommandContext context) {

        if (!hasArguments(context)) {

            sender.sendMessage("=== Help ===");

            List<VanillaCommandsFeature> commands = new ArrayList<>();

            Collections.addAll(commands, VanillaCommandsFeature.values());

            commands.sort(this::compareCommands);

            commands.forEach(command -> sender.sendMessage("/" + command.name().toLowerCase()));

            sender.sendMessage("============");

            return;
        }

        sender.sendMessage(Component.text("Unknown command: ").color(NamedTextColor.RED)
                .append(Component.text(context.getInput().replace(context.getCommandName() + " ","")).color(NamedTextColor.RED).decoration(TextDecoration.UNDERLINED, true)));


    }
}
