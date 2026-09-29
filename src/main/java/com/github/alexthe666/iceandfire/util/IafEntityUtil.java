package com.github.alexthe666.iceandfire.util;

import net.minecraft.server.level.ServerLevel;
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
}
