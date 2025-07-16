package net.minestom.vanilla.commands.all;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.ConsoleSender;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentString;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.entity.Player;
import net.minestom.vanilla.commands.VanillaCommand;

public class MsgCommand extends VanillaCommand {
    public MsgCommand() {
        super("msg", "tell", "w");

        setCondition(permission(LEVEL_ALL));

        setDefaultExecutor(this::defaultor);

        ArgumentEntity targetArg = ArgumentType.Entity("targets").onlyPlayers(true);
        ArgumentString messageArg = ArgumentType.String("message");

        addSyntax((sender, context) -> {
            Player player = context.get(targetArg).findFirstPlayer(sender);
            String message = context.get(messageArg);

            if (player == null) {
                sender.sendMessage(Component.text("No player was found", NamedTextColor.RED));
                return;
            }

            String targetName = player.getUsername();
            String formattedMessage = "whispers to you: " + message;

            if (sender instanceof ConsoleSender) {
                player.sendMessage(Component.text("Server " + formattedMessage).decorate(TextDecoration.ITALIC).color(NamedTextColor.GRAY));
                return;
            }

            String senderName = ((Player) sender).getUsername();
            sender.sendMessage(Component.text("You whisper to " + targetName + ": " + message).decorate(TextDecoration.ITALIC).color(NamedTextColor.GRAY));
            player.sendMessage(Component.text(senderName + " " + formattedMessage).decorate(TextDecoration.ITALIC).color(NamedTextColor.GRAY));

        }, targetArg, messageArg);


    }

    @Override
    public Component usage(CommandSender sender, CommandContext context) {
        return Component.text("/msg <targets> <message>");
    }

    @Override
    protected void defaultor(CommandSender sender, CommandContext context) {
        sender.sendMessage(Component.text("Unknown or incomplete command").color(NamedTextColor.RED)
                .appendNewline()
                .append(Component.text(context.getInput().replace(context.getCommandName() + " ","")).color(NamedTextColor.GRAY)
                        .append(Component.text("<--[HERE]").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC,true))));
    }
}
