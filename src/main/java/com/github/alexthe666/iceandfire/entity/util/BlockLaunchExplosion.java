package com.github.alexthe666.iceandfire.entity.util;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * Small adapter for the 26.2 explosion API. Calls to {@link #explode()} perform the
 * explosion; {@link #finalizeExplosion(boolean)} remains for the legacy call sequence.
 */
public final class BlockLaunchExplosion {
    private final Level level;
    private final Mob source;
    private final DamageSource damageSource;
    private final double x;
    private final double y;
    private final double z;
    private final float radius;
    private final Explosion.BlockInteraction blockInteraction;
    private boolean exploded;

    public BlockLaunchExplosion(Level level, Mob source, double x, double y, double z, float radius) {
        this(level, source, null, x, y, z, radius, Explosion.BlockInteraction.DESTROY);
    }

    public BlockLaunchExplosion(Level level, Mob source, double x, double y, double z, float radius, Explosion.BlockInteraction blockInteraction) {
        this(level, source, null, x, y, z, radius, blockInteraction);
    }

    public BlockLaunchExplosion(Level level, Mob source, DamageSource damageSource, double x, double y, double z, float radius, Explosion.BlockInteraction blockInteraction) {
        this.level = level;
        this.source = source;
        this.damageSource = damageSource;
        this.x = x;
        this.y = y;
        this.z = z;
        this.radius = radius;
        this.blockInteraction = blockInteraction;
    }

    public void explode() {
        if (this.exploded) {
            return;
        }
        this.exploded = true;
        Level.ExplosionInteraction interaction = switch (this.blockInteraction) {
            case KEEP -> Level.ExplosionInteraction.NONE;
            case DESTROY, DESTROY_WITH_DECAY -> Level.ExplosionInteraction.BLOCK;
            case TRIGGER_BLOCK -> Level.ExplosionInteraction.TRIGGER;
        };
        DamageSource damage = this.damageSource == null ? Explosion.getDefaultDamageSource(this.level, this.source) : this.damageSource;
        this.level.explode(this.source, damage, null, new Vec3(this.x, this.y, this.z), this.radius, false, interaction);
    }

    public void finalizeExplosion(boolean spawnParticles) {
        // NeoForge 26.2 creates explosion particles and sounds as part of Level.explode.
    }
}
