package com.nicktale.api.server.entity.collision;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/** Lets an entity decide per block whether its collision shape blocks movement. */
public interface ICustomCollisions {

    /** @return true if the entity may move through this block despite its collision shape. */
    boolean canPassThrough(BlockPos pos, BlockState state, VoxelShape shape);

    static Vec3 getAllowedMovementForEntity(Entity entity, Vec3 movement) {
        if (movement.lengthSqr() == 0.0D) {
            return movement;
        }
        AABB box = entity.getBoundingBox();
        List<VoxelShape> shapes = collect(entity, box.expandTowards(movement));
        Vec3 result = collideAxes(movement, box, shapes);
        boolean horizontalBlocked = result.x != movement.x || result.z != movement.z;
        float step = entity.maxUpStep();
        if (step > 0.0F && entity.onGround() && horizontalBlocked && movement.y <= 0.0D) {
            List<VoxelShape> stepShapes = collect(entity, box.expandTowards(movement.x, step, movement.z));
            Vec3 up = collideAxes(new Vec3(movement.x, step, movement.z), box, stepShapes);
            Vec3 upOnly = collideAxes(new Vec3(0.0D, step, 0.0D), box, stepShapes);
            if (upOnly.y < step) {
                Vec3 across = collideAxes(new Vec3(movement.x, 0.0D, movement.z), box.move(upOnly), stepShapes).add(upOnly);
                if (across.horizontalDistanceSqr() > up.horizontalDistanceSqr()) {
                    up = across;
                }
            }
            if (up.horizontalDistanceSqr() > result.horizontalDistanceSqr()) {
                Vec3 down = collideAxes(new Vec3(0.0D, -up.y + movement.y, 0.0D), box.move(up), stepShapes);
                return up.add(down);
            }
        }
        return result;
    }

    private static Vec3 collideAxes(Vec3 movement, AABB box, List<VoxelShape> shapes) {
        double x = movement.x;
        double y = movement.y;
        double z = movement.z;
        if (y != 0.0D) {
            y = Shapes.collide(net.minecraft.core.Direction.Axis.Y, box, shapes, y);
            if (y != 0.0D) {
                box = box.move(0.0D, y, 0.0D);
            }
        }
        boolean zFirst = Math.abs(x) < Math.abs(z);
        if (zFirst && z != 0.0D) {
            z = Shapes.collide(net.minecraft.core.Direction.Axis.Z, box, shapes, z);
            if (z != 0.0D) {
                box = box.move(0.0D, 0.0D, z);
            }
        }
        if (x != 0.0D) {
            x = Shapes.collide(net.minecraft.core.Direction.Axis.X, box, shapes, x);
            if (!zFirst && x != 0.0D) {
                box = box.move(x, 0.0D, 0.0D);
            }
        }
        if (!zFirst && z != 0.0D) {
            z = Shapes.collide(net.minecraft.core.Direction.Axis.Z, box, shapes, z);
        }
        return new Vec3(x, y, z);
    }

    private static List<VoxelShape> collect(Entity entity, AABB area) {
        Level level = entity.level();
        List<VoxelShape> shapes = new ArrayList<>(level.getEntityCollisions(entity, area));
        ICustomCollisions custom = entity instanceof ICustomCollisions c ? c : null;
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int x = (int) Math.floor(area.minX) - 1; x <= (int) Math.floor(area.maxX) + 1; x++) {
            for (int y = (int) Math.floor(area.minY) - 1; y <= (int) Math.floor(area.maxY) + 1; y++) {
                for (int z = (int) Math.floor(area.minZ) - 1; z <= (int) Math.floor(area.maxZ) + 1; z++) {
                    pos.set(x, y, z);
                    BlockState state = level.getBlockState(pos);
                    VoxelShape shape = state.getCollisionShape(level, pos, net.minecraft.world.phys.shapes.CollisionContext.of(entity));
                    if (shape.isEmpty() || (custom != null && custom.canPassThrough(pos, state, shape))) {
                        continue;
                    }
                    shapes.add(shape.move(x, y, z));
                }
            }
        }
        return shapes;
    }
}
