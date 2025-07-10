package net.minestom.vanilla.commands.gamemaster;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.world.Difficulty;
import net.minestom.vanilla.commands.VanillaCommand;

/**
 * Command that make an instance change difficulty
 */
public class DifficultyCommand extends VanillaCommand {

    public DifficultyCommand() {
        super("difficulty");

        setCondition(permission(LEVEL_GAMEMASTER));

        setDefaultExecutor(this::defaultor);

        ArgumentEnum<Difficulty> difficulty = ArgumentType.Enum("difficulty", Difficulty.class).setFormat(ArgumentEnum.Format.LOWER_CASED);

        addSyntax((sender, context) -> {
            Difficulty newDifficulty = context.get(difficulty);
            setDifficulty(sender, newDifficulty);
        }, difficulty);
    }

    @Override
    public Component usage(CommandSender sender, CommandContext context) {
        return Component.text("/difficulty [peaceful]")
                .appendNewline()
                .append(Component.text("/difficulty [easy]"))
                .appendNewline()
                .append(Component.text("/difficulty [normal]"))
                .appendNewline()
                .append(Component.text("/difficulty [hard]"));
    }

    @Override
    public void defaultor(CommandSender sender, CommandContext context) {
        if (hasNoArguments(context)) {
            setDifficulty(sender, Difficulty.EASY);
            return;
        }

        sender.sendMessage(Component.text("Unknown difficulty: ").color(NamedTextColor.RED)
                .append(Component.text(context.getInput().replace(context.getCommandName() + " ","")).color(NamedTextColor.RED).decoration(TextDecoration.UNDERLINED, true)));


    }


    private void setDifficulty(CommandSender sender, Difficulty difficulty) {
        Difficulty current = MinecraftServer.getDifficulty();

        if (current == difficulty) {
            sender.sendMessage(Component.text("Difficulty already set to " + difficulty.name().toLowerCase()).color(NamedTextColor.RED));
            return;
        }

        MinecraftServer.setDifficulty(difficulty);
        sender.sendMessage(Component.text("You are now playing in " + difficulty.name().toLowerCase()));
    }
}

