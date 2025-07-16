package net.minestom.vanilla.commands;

import net.minestom.server.MinecraftServer;
import net.minestom.server.command.CommandSender;
import net.minestom.server.command.builder.CommandContext;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.world.Difficulty;
import net.minestom.vanilla.commands.gamemaster.DifficultyCommand;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class DifficultyCommandTest {

    private DifficultyCommand command;

    @BeforeEach
    void setup() {
        command = new DifficultyCommand();
        MinecraftServer.setDifficulty(Difficulty.NORMAL);
    }

    @AfterEach
    void cleanup() {
        MinecraftServer.setDifficulty(Difficulty.NORMAL);
    }

    @Test
    void setsDifficultyToPeaceful() {
        CommandSender sender = mock(CommandSender.class);
        CommandContext context = mock(CommandContext.class);

        when(context.get(anyString())).thenReturn(Difficulty.PEACEFUL);
        when(context.get(any(Argument.class))).thenReturn(Difficulty.PEACEFUL);

        command.getSyntaxes().stream().toList().getFirst().getExecutor().apply(sender, context);

        assertEquals(Difficulty.PEACEFUL, MinecraftServer.getDifficulty());
    }

    @Test
    void showsErrorWhenAlreadySet() {

    }

    @Test
    void defaultsToEasyWhenNoArg() {

    }

    @Test
    void showsErrorOnInvalidInput() {

    }
}
