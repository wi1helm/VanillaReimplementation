package net.minestom.vanilla.commands.all;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentCommand;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import net.minestom.vanilla.commands.VanillaCommand;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ListCommand extends VanillaCommand {
    public ListCommand() {
        super("list");

        setCondition(permission(LEVEL_ALL));

        setDefaultExecutor(this::defaultor);

        ArgumentCommand arg = ArgumentType.Command("uuids");

        addSyntax((sender, context) -> {
            listPlayers(sender, context, true);
        }, arg);

    }

    @Override
    public Component usage(CommandSender sender, CommandContext context) {
        return Component.text("/list [uuids]");
    }

    @Override
    public void defaultor(CommandSender sender, CommandContext context) {
        listPlayers(sender, context, false);
    }

    private void listPlayers(CommandSender sender, CommandContext context, boolean uuid) {
        int onlinePlayersCount = MinecraftServer.getConnectionManager().getOnlinePlayerCount();
        String maxPlayers = "???";
        //TODO add maxPlayerCount thingy from sever config / properties i think

        Collection<Player> onlinePlayers = MinecraftServer.getConnectionManager().getOnlinePlayers();

        List<Component> playerComponents = onlinePlayers.stream()
                .map(player -> {
                    // Start with the player's username
                    Component playerEntry = Component.text(player.getUsername());
                    if (uuid) {
                        // If UUIDs are requested, append "(UUID)" to the username
                        playerEntry = playerEntry.append(Component.text(" (" + player.getUuid() + ")"));
                    }
                    return playerEntry;
                })
                .collect(Collectors.toList());

        Component playerListComponent = Component.join(
                JoinConfiguration.builder().separator(Component.text(", ")).build(),
                playerComponents
        );

        Component headerComponent = Component.text("There are ")
                .append(Component.text(onlinePlayersCount))
                .append(Component.text(" of a max of "))
                .append(Component.text(maxPlayers))
                .append(Component.text(" players online: "));

        Component fullMessage = headerComponent.append(playerListComponent);

        sender.sendMessage(fullMessage);
    }
}
