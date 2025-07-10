package net.minestom.vanilla.commands.gamemaster;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.ArgumentEnum;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.command.builder.arguments.minecraft.ArgumentEntity;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.vanilla.commands.VanillaCommand;

import java.util.List;

/**
 * Command that make a player change gamemode, made in
 * the style of the vanilla /gamemode command.
 *
 * @see <a href="https://minecraft.fandom.com/wiki/Commands/gamemode">...</a>
 */
public class GamemodeCommand extends VanillaCommand {

    public GamemodeCommand() {
        super("gamemode");

        setCondition(permission(LEVEL_GAMEMASTER));

        ArgumentEnum<GameMode> gamemode = ArgumentType.Enum("gamemode", GameMode.class).setFormat(ArgumentEnum.Format.LOWER_CASED);

        ArgumentEntity target = ArgumentType.Entity("targets").onlyPlayers(true);

        setDefaultExecutor(this::defaultor);

        //Command Syntax for /gamemode <gamemode>
        addSyntax((sender, context) -> {
            GameMode mode = context.get(gamemode);

            setGamemodeSelf(sender, mode);

        }, gamemode);

        //Command Syntax for /gamemode <gamemode> [targets]
        addSyntax((sender, context) -> {
            List<Entity> entities = context.get(target).find(sender);
            GameMode mode = context.get(gamemode);

            //Set the gamemode for the targets
            setGamemodeOther(sender, mode, entities);
        }, gamemode, target);
    }


    @Override
    public Component usage(CommandSender sender, CommandContext context) {
        return Component.text("/gamemode <gamemode> [<targets>]");
    }

    @Override
    public void defaultor(CommandSender sender, CommandContext context) {

        if (hasNoArguments(context)) {
            sender.sendMessage(Component.text("Unknown or incomplete command").color(NamedTextColor.RED)
                    .appendNewline()
                    .append(Component.text(context.getInput().replace(context.getCommandName() + " ","")).color(NamedTextColor.GRAY)
                    .append(Component.text("<--[HERE]").color(NamedTextColor.RED).decoration(TextDecoration.ITALIC,true))));
            return;
        }

        sender.sendMessage(Component.text("Unknown gamemode: ").color(NamedTextColor.RED)
                .append(Component.text(context.getInput().replace(context.getCommandName() + " ","")).color(NamedTextColor.RED).decoration(TextDecoration.UNDERLINED, true)));
    }

    private void setGamemodeSelf(CommandSender sender, GameMode gameMode) {
        if (!(sender instanceof final Player playerSender)) {
            sender.sendMessage(Component.text("When executed from console, targets must be specified.", NamedTextColor.RED));
            return;
        };
        setGamemode(playerSender, gameMode);
    }

    private void setGamemodeOther(CommandSender sender, GameMode gameMode, List<Entity> entities) {
        if (entities.isEmpty()) {
            sender.sendMessage(Component.text("No player was found", NamedTextColor.RED));
            return;
        }

        for (Entity entity : entities) {

            setGamemode(entity, gameMode);
        }

        sender.sendMessage(Component.text("Updated gamemode:)"));
    }


    private void setGamemode(Entity entity, GameMode gamemode) {

        final Player player = (Player) entity;

        player.setGameMode(gamemode);
        player.sendMessage("You are now in " + gamemode.name().toLowerCase());
    }
}
