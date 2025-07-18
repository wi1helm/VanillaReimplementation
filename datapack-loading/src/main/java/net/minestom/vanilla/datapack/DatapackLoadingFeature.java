package net.minestom.vanilla.datapack;

import io.github.pesto.MojangDataFeature;
import net.kyori.adventure.key.Key;
import net.minestom.vanilla.VanillaReimplementation;
import net.minestom.vanilla.files.ByteArray;
import net.minestom.vanilla.files.FileSystem;
import net.minestom.vanilla.logging.Loading;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Objects;
import java.util.Set;

public class DatapackLoadingFeature implements VanillaReimplementation.Feature {

    private @UnknownNullability Datapack datapack;

    @Override
    public void hook(@NotNull HookContext context) {

        @NotNull MojangDataFeature data = context.vri().feature(MojangDataFeature.class);

        Loading.start("Parsing vanilla datapack");
        FileSystem<ByteArray> fs = data.latestAssets();
        datapack = Datapack.loadByteArray(fs);
        Loading.finish();
    }

    public @NotNull Datapack current() {
        Objects.requireNonNull(datapack, "DatapackLoadingFeature not loaded yet");
        return datapack;
    }

    @Override
    public @NotNull Key key() {
        return Key.key("vri:datapack");
    }

    @Override
    public @NotNull Set<Class<? extends VanillaReimplementation.Feature>> dependencies() {
        return Set.of(MojangDataFeature.class);
    }
}
