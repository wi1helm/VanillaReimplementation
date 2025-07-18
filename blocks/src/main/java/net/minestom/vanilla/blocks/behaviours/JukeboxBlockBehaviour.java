package net.minestom.vanilla.blocks.behaviours;

import net.minestom.server.MinecraftServer;
import net.minestom.server.component.DataComponents;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.ItemEntity;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerHand;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.block.jukebox.JukeboxSong;
import net.minestom.server.item.ItemStack;
import net.minestom.server.registry.DynamicRegistry;
import net.minestom.server.tag.Tag;
import net.minestom.server.worldevent.WorldEvent;
import net.minestom.vanilla.blocks.VanillaBlockBehaviour;
import net.minestom.vanilla.blocks.VanillaBlocks;
import net.minestom.vanilla.inventory.InventoryManipulation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Random;

/**
 * Reimplementation of the jukebox block
 * <p>
 * Requires onPlace enhancements
 */
public class JukeboxBlockBehaviour extends VanillaBlockBehaviour {

    public static final Tag<ItemStack> DISC_KEY = Tag.ItemStack("minestom:jukebox_disc");

    public JukeboxBlockBehaviour(@NotNull VanillaBlocks.BlockContext context) {
        super(context);
    }

    @Override
    public void onDestroy(@NotNull Destroy destroy) {
        if (!(destroy instanceof PlayerDestroy)) return;
        stopPlayback(destroy.getInstance(), destroy.getBlockPosition(), destroy.getBlock());
    }

    public @Nullable ItemStack getDisc(Block block) {
        return block.getTag(DISC_KEY);
    }

    public @NotNull Block withDisc(Block block, @NotNull ItemStack disc) {
        if (isNotMusicDisc(disc)) {
            throw new IllegalArgumentException("disc passed to JukeboxBlockHandle#withDisc was not a music disc.");
        }
        return block.withTag(DISC_KEY, disc);
    }

    private boolean isNotMusicDisc(ItemStack itemStack) {
        return !itemStack.has(DataComponents.JUKEBOX_PLAYABLE);
    }

    @Override
    public boolean onInteract(@NotNull Interaction interaction) {
        Player player = interaction.getPlayer();
        PlayerHand hand = interaction.getHand();
        Instance instance = interaction.getInstance();
        Block block = interaction.getBlock();
        Point pos = interaction.getBlockPosition();
        ItemStack heldItem = player.getItemInMainHand();

        ItemStack stack = this.getDisc(block);

        if (stack != null && !stack.isAir()) {
            stopPlayback(instance, pos, block);
            block = block.withTag(DISC_KEY, ItemStack.AIR);
            instance.setBlock(pos, block.withProperty("has_record", "false"));
            return true;
        }

        if (isNotMusicDisc(heldItem)) {
            return true;
        }

        instance.setBlock(pos, withDisc(block, heldItem).withProperty("has_record", "true"));

        InventoryManipulation.consumeItemIfNotCreative(player, heldItem, hand);

        JukeboxSong song = heldItem.get(DataComponents.JUKEBOX_PLAYABLE).holder().resolve(MinecraftServer.getJukeboxSongRegistry());
        DynamicRegistry.Key<JukeboxSong> songKey = MinecraftServer.getJukeboxSongRegistry().getKey(song);
        int songId = MinecraftServer.getJukeboxSongRegistry().getId(songKey);

        // TODO: Group packet?
        instance.getPlayers()
                .stream()
                .filter(player1 -> player1.getDistance(pos) < 64)
                .forEach(player1 ->
                        player1.playEffect(
                                WorldEvent.SOUND_PLAY_JUKEBOX_SONG,
                                pos.blockX(),
                                pos.blockY(),
                                pos.blockZ(),
                                songId,
                                false
                        )
                );

        return true;
    }

    @Override
    public boolean isTickable() {
        return true;
    }

    public void tick(@NotNull Tick tick) {
        Instance instance = tick.getInstance();

        long age = instance.getWorldAge();

        // Continue only every 3 seconds
        if (age % (MinecraftServer.TICK_PER_SECOND * 3L) != 0) {
        }

        // TODO: Play sound to all players without the sound playing
    }

//    @Override
//    public Data readBlockEntity(NBTCompound nbt, Instance instance, BlockPosition position, Data originalData) {
//        JukeboxBlockEntity data;
//        if (originalData instanceof JukeboxBlockEntity) {
//            data = (JukeboxBlockEntity) originalData;
//        } else {
//            data = new JukeboxBlockEntity(position.copy());
//        }
//
//        if(nbt.containsKey("RecordItem")) {
//            data.setDisc(ItemStack.fromNBT(nbt.getCompound("RecordItem")));
//        }
//        return super.readBlockEntity(nbt, instance, position, originalData);
//    }
//
//    @Override
//    public void writeBlockEntity(BlockPosition position, Data blockData, NBTCompound nbt) {
//        if(blockData instanceof JukeboxBlockEntity) {
//            JukeboxBlockEntity data = (JukeboxBlockEntity) blockData;
//            nbt.set("RecordItem", data.getDisc().toNBT());
//        }
//    }

    /**
     * Stops playback in an instance
     */
    private void stopPlayback(Instance instance, Point pos, Block block) {
        ItemEntity discEntity = new ItemEntity(Objects.requireNonNull(getDisc(block)));
        discEntity.setInstance(instance);
        discEntity.teleport(new Pos(pos.x() + 0.5f, pos.y() + 1f, pos.z() + 0.5f));
        discEntity.setPickable(true);

        Random rng = new Random();
        final float horizontalSpeed = 2f;
        final float verticalSpeed = 5f;

        discEntity.setVelocity(new Vec(
                rng.nextGaussian() * horizontalSpeed,
                rng.nextFloat() * verticalSpeed,
                rng.nextGaussian() * horizontalSpeed
        ));

        discEntity.setInstance(instance);

        // TODO: Group Packet?
        instance.getPlayers().forEach(playerInInstance -> {
            // stop playback
            playerInInstance.playEffect(
                    WorldEvent.SOUND_STOP_JUKEBOX_SONG,
                    pos.blockX(),
                    pos.blockY(),
                    pos.blockZ(),
                    -1,
                    false
            );
        });
    }
}
