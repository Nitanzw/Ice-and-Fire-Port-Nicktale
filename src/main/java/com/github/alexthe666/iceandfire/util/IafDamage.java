package com.github.alexthe666.iceandfire.util;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;

/** Damage entry point for code written against the pre-1.21.2 {@code Entity.hurt}, which only exists on the server now. */
public final class IafDamage {
    private IafDamage() {
    }

    public static boolean hurt(Entity target, DamageSource source, float amount) {
        if (target != null && target.level() instanceof ServerLevel serverLevel) {
            return target.hurtServer(serverLevel, source, amount);
        }
        return false;
    }
}
