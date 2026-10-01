package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.entity.tile.TileEntityPixieHouse;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;
import com.mojang.serialization.MapCodec;

import javax.annotation.Nullable;

import static com.github.alexthe666.iceandfire.entity.tile.IafTileEntityRegistry.PIXIE_HOUSE;

public class BlockPixieHouse extends BaseEntityBlock {
    //#if MC < 26.3
    private static final MapCodec<BlockPixieHouse> CODEC = MapCodec.unit(BlockPixieHouse::new);
    //#endif
    public static final net.minecraft.world.level.block.state.properties.EnumProperty<Direction> FACING =
        net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

    public BlockPixieHouse() {
        super(
            com.github.alexthe666.iceandfire.block.IafBlockProps.of()
                .mapColor(MapColor.WOOD)
                .instrument(NoteBlockInstrument.BASS)
                .ignitedByLava()
                .noOcclusion()
                .dynamicShape()
                .strength(2.0F, 5.0F)
                .randomTicks()
		);
        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH));
    }

    //#if MC < 26.3
    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
    //#endif

    static String name(String type) {
        return "pixie_house_%s".formatted(type);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> entityType) {
        return level.isClientSide() ? createTickerHelper(entityType, PIXIE_HOUSE.get(), TileEntityPixieHouse::tickClient) : createTickerHelper(entityType, PIXIE_HOUSE.get(), TileEntityPixieHouse::tickServer);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new TileEntityPixieHouse(pos, state);
    }
}
