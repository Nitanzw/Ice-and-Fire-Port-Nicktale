package com.github.alexthe666.iceandfire.pathfinding;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.PathfindingContext;
import net.minecraft.world.level.pathfinder.Target;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

/**
 * Path evaluator for death worms that treats sand and open air as burrowable
 * space and lets the worm surface at the boundary of a tunnel.
 */
public class NodeProcessorDeathWorm extends NodeEvaluator {

    @Override
    public @NotNull Node getStart() {
        return this.getNode(
            Mth.floor(this.mob.getBoundingBox().minX),
            Mth.floor(this.mob.getBoundingBox().minY + 0.5D),
            Mth.floor(this.mob.getBoundingBox().minZ)
        );
    }

    @Override
    public @NotNull Target getTarget(double x, double y, double z) {
        return new Target(this.getNode(Mth.floor(x - 0.4D), Mth.floor(y + 0.5D), Mth.floor(z - 0.4D)));
    }

    @Override
    public @NotNull PathType getPathTypeOfMob(@NotNull PathfindingContext context, int x, int y, int z, @NotNull Mob mob) {
        return this.getPathType(context, x, y, z);
    }

    @Override
    public @NotNull PathType getPathType(@NotNull PathfindingContext context, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        BlockState state = context.getBlockState(pos);
        if (!isPassable(context, pos.below()) && (state.isAir() || isPassable(context, pos))) {
            return PathType.BREACH;
        }
        return isPassable(context, pos) ? PathType.WATER : PathType.BLOCKED;
    }

    @Override
    public int getNeighbors(Node @NotNull [] neighbors, @NotNull Node current) {
        int count = 0;
        for (Direction direction : Direction.values()) {
            Node next = this.getSandNode(
                current.x + direction.getStepX(),
                current.y + direction.getStepY(),
                current.z + direction.getStepZ()
            );
            if (next != null && !next.closed) {
                neighbors[count++] = next;
            }
        }
        return count;
    }

    @Nullable
    private Node getSandNode(int x, int y, int z) {
        PathType type = this.isFree(x, y, z);
        return type == PathType.BREACH || type == PathType.WATER ? this.getNode(x, y, z) : null;
    }

    private PathType isFree(int x, int y, int z) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int dx = 0; dx < this.entityWidth; dx++) {
            for (int dy = 0; dy < this.entityHeight; dy++) {
                for (int dz = 0; dz < this.entityDepth; dz++) {
                    pos.set(x + dx, y + dy, z + dz);
                    BlockState state = this.currentContext.getBlockState(pos);
                    if (!isPassable(this.currentContext, pos.below())
                        && (state.isAir() || isPassable(this.currentContext, pos))) {
                        return PathType.BREACH;
                    }
                }
            }
        }

        return isPassable(this.currentContext, pos) ? PathType.WATER : PathType.BLOCKED;
    }

    private static boolean isPassable(PathfindingContext context, BlockPos pos) {
        BlockState state = context.getBlockState(pos);
        return state.is(BlockTags.SAND) || state.isAir();
    }
}
