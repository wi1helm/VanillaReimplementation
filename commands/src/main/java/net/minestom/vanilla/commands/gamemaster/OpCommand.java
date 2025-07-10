package net.minestom.vanilla.commands.gamemaster;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.MinecraftServer;
import net.minestom.server.adventure.audience.Audiences;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.Player;
import net.minestom.vanilla.commands.VanillaCommand;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OpCommand extends VanillaCommand {
    public OpCommand() {
        super("op");

        setCondition(permission(LEVEL_ALL));

        ArgumentEntity target = ArgumentType.Entity("target").onlyPlayers(true);

        setDefaultExecutor(this::defaultor);

        addSyntax((sender, context) -> {
            List<Entity> entities = context.get(target).find(sender);

            setOp(sender, context, entities);
        }, target);
    }

    private void setOp(CommandSender sender, CommandContext context, List<Entity> entities) {
        if (entities.isEmpty()) {
            sender.sendMessage(Component.text("No player was found", NamedTextColor.RED));
            return;
        }

        for (Entity entity : entities) {

            final Player player = (Player) entity;

            if (player.getPermissionLevel() >= LEVEL_GAMEMASTER) {
                player.sendMessage(Component.text("Nothing changed. The player is already operator").color(NamedTextColor.RED));
                return;
            }

            player.setPermissionLevel(LEVEL_GAMEMASTER);
            player.refreshCommands();

            MinecraftServer.getConnectionManager().getOnlinePlayers().stream()
                    .filter(p -> player.getPermissionLevel() >= LEVEL_GAMEMASTER) // Filter for players with operator permission
                    .forEach(p -> player.sendMessage(Component.text("[Server: Made " + player.getUsername() + " a server operator]").color(NamedTextColor.GRAY).decoration(TextDecoration.ITALIC, true))); // Send the constructed message

        }
    }


    @Override
    public Component usage(CommandSender sender, CommandContext context) {
        return Component.text("/op [<targets>]");
    }

    @Override
    public void defaultor(CommandSender sender, CommandContext context) {
        sender.sendMessage(Component.text("Unknown or incomplete command").color(NamedTextColor.RED)
                .appendNewline()
                .append(Component.text(context.getInput().replace(context.getCommandName() + " ","")).color(NamedTextColor.GRAY)
                        .append(Component.text("<--[HERE]").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC,true))));
    }
}
