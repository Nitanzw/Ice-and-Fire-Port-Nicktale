package com.github.alexthe666.iceandfire.block;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.material.MapColor;

public class BlockFallingGeneric extends FallingBlock {
    private static final MapCodec<BlockFallingGeneric> CODEC = BlockBehaviour.simpleCodec(BlockFallingGeneric::new);
    public Item itemBlock;

/*    public BlockFallingGeneric(float hardness, float resistance, SoundType sound) {
        super(
            BlockBehaviour.Properties
                .of()
                .sound(sound)
                .strength(hardness, resistance)
        );
    }

    @SuppressWarnings("deprecation")
    public BlockFallingGeneric(float hardness, float resistance, SoundType sound, boolean slippery) {
        super(
            BlockBehaviour.Properties
                .of()
                .sound(sound)
                .strength(hardness, resistance)
                .friction(0.98F)
        );
    }*/

    public static BlockFallingGeneric builder(float hardness, float resistance, SoundType sound, MapColor color, NoteBlockInstrument instrument) {
        BlockBehaviour.Properties props = BlockBehaviour.Properties.of()
                .mapColor(color)
                .instrument(instrument)
                .sound(sound)
                .strength(hardness, resistance);
        return new BlockFallingGeneric(props);
    }

    public BlockFallingGeneric(BlockBehaviour.Properties props) {
        super(props);
    }

    @Override
    protected MapCodec<? extends FallingBlock> codec() {
        return CODEC;
    }

    @Override
    public int getDustColor(BlockState blkst, BlockGetter level, BlockPos pos) {
        return -8356741;
    }
}
