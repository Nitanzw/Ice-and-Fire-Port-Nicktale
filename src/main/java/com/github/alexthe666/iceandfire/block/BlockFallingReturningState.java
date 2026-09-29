package com.github.alexthe666.iceandfire.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;
import com.mojang.serialization.MapCodec;

public class BlockFallingReturningState extends FallingBlock {
    private static final MapCodec<BlockFallingReturningState> CODEC = BlockBehaviour.simpleCodec(properties ->
        new BlockFallingReturningState(0.6F, 0.0F, SoundType.GRAVEL, properties, Blocks.GRAVEL.defaultBlockState()));
    public static final BooleanProperty REVERTS = BooleanProperty.create("revert");
    public Item itemBlock;
    private final BlockState returnState;

    private BlockFallingReturningState(float hardness, float resistance, SoundType sound, BlockBehaviour.Properties properties, BlockState revertState) {
        super(properties);
        this.returnState = revertState;
        this.registerDefaultState(this.stateDefinition.any().setValue(REVERTS, Boolean.FALSE));
    }

    public BlockFallingReturningState(float hardness, float resistance, SoundType sound, MapColor color, BlockState revertState) {
        super(
            com.github.alexthe666.iceandfire.block.IafBlockProps.of()
                .mapColor(color)
                .sound(sound)
                .strength(hardness, resistance)
                .randomTicks()
        );

        this.returnState = revertState;
        this.registerDefaultState(this.stateDefinition.any().setValue(REVERTS, Boolean.FALSE));
    }

    @SuppressWarnings("deprecation")
    public BlockFallingReturningState(float hardness, float resistance, SoundType sound, boolean slippery, MapColor color, BlockState revertState) {
        super(
            com.github.alexthe666.iceandfire.block.IafBlockProps.of()
                .mapColor(color)
                .sound(sound)
                .strength(hardness, resistance)
                .randomTicks()
        );

        this.returnState = revertState;
        this.registerDefaultState(this.stateDefinition.any().setValue(REVERTS, Boolean.FALSE));
    }

    @Override
    public void tick(@NotNull BlockState state, @NotNull ServerLevel worldIn, @NotNull BlockPos pos, @NotNull RandomSource rand) {
        super.tick(state, worldIn, pos, rand);
        if (!worldIn.isClientSide()) {
            if (!worldIn.isAreaLoaded(pos, 3))
                return;
            if (state.getValue(REVERTS) && rand.nextInt(3) == 0) {
                worldIn.setBlockAndUpdate(pos, returnState);
            }
        }
    }


    @Override
    protected MapCodec<? extends FallingBlock> codec() {
        return CODEC;
    }

    @Override
    public int getDustColor(BlockState blkst, BlockGetter level, BlockPos pos) {
        return -8356741;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(REVERTS);
    }
}
