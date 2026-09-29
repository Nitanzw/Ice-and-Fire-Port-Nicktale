package com.github.alexthe666.iceandfire.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gamerules.GameRule;

import javax.annotation.Nullable;
import java.util.UUID;

/** Small helpers that keep call sites written for 1.20 (owner uuid, game rules) working on 26.x. */
public final class IafEntityUtil {
    private IafEntityUtil() {
    }

    @Nullable
    public static UUID ownerUUID(TamableAnimal animal) {
        var ref = animal.getOwnerReference();
        return ref == null ? null : ref.getUUID();
    }

    /** Game rules only exist on the server; the client sees the default (true) so cosmetic code keeps running. */
    public static boolean gameRule(Level level, GameRule<Boolean> rule) {
        if (level instanceof ServerLevel serverLevel) {
            return serverLevel.getGameRules().get(rule);
        }
        return true;
    }

    /** Drops an item at the entity (server only; the client never spawns drops). */
    @Nullable
    public static ItemEntity drop(Entity entity, ItemStack stack) {
        return entity.level() instanceof ServerLevel serverLevel ? entity.spawnAtLocation(serverLevel, stack) : null;
    }

    @Nullable
    public static ItemEntity drop(Entity entity, ItemStack stack, float yOffset) {
        return entity.level() instanceof ServerLevel serverLevel
                ? entity.spawnAtLocation(serverLevel, stack, new net.minecraft.world.phys.Vec3(0.0D, yOffset, 0.0D)) : null;
    }

    @Nullable
    public static ItemEntity drop(Entity entity, ItemLike item) {
        return drop(entity, new ItemStack(item));
    }

    public static Ingredient ingredient(TagKey<Item> tag) {
        return Ingredient.of(BuiltInRegistries.ITEM.getOrThrow(tag));
    }

    /** Replacement for the removed {@code Mob.setMaxUpStep}. */
    public static void setStepHeight(LivingEntity entity, double height) {
        AttributeInstance attribute = entity.getAttribute(Attributes.STEP_HEIGHT);
        if (attribute != null) {
            attribute.setBaseValue(height);
        }
    }

    /** Melee attack helper: doHurtTarget needs a ServerLevel now. */
    public static boolean attack(net.minecraft.world.entity.Mob attacker, Entity target) {
        return attacker.level() instanceof ServerLevel serverLevel && attacker.doHurtTarget(serverLevel, target);
    }

    /** Old three-argument knockback. */
    public static void knockback(LivingEntity target, double power, double xd, double zd) {
        target.knockback(power, xd, zd, target.level().damageSources().generic(), 0.0F);
    }

    /** Adapts an old {@code Predicate<LivingEntity>} target filter to the level-aware selector. */
    public static net.minecraft.world.entity.ai.targeting.TargetingConditions.Selector selector(java.util.function.Predicate<? super LivingEntity> predicate) {
        return (target, level) -> predicate.test(target);
    }

    public static net.minecraft.world.DifficultyInstance difficulty(Level level, net.minecraft.core.BlockPos pos) {
        if (level instanceof ServerLevel serverLevel) {
            return IafEntityUtil.difficulty(serverLevel, pos);
        }
        return new net.minecraft.world.DifficultyInstance(level.getDifficulty(), 0L, 0L, 0.0F);
    }

    public static java.util.Optional<net.minecraft.world.entity.EntityType<?>> entityTypeByString(String id) {
        net.minecraft.resources.Identifier location = net.minecraft.resources.Identifier.tryParse(id);
        return location == null ? java.util.Optional.empty() : BuiltInRegistries.ENTITY_TYPE.getOptional(location);
    }
}
