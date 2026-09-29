package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.util.IafDamage;
import net.minecraft.server.level.ServerLevel;
import com.github.alexthe666.iceandfire.entity.util.DragonUtils;
import com.github.alexthe666.iceandfire.entity.util.IDragonProjectile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.projectile.hurtingprojectile.Fireball;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public abstract class EntityDragonCharge extends Fireball implements IDragonProjectile {


    public EntityDragonCharge(EntityType<? extends Fireball> type, Level worldIn) {
        super(type, worldIn);
        this.accelerationPower = 0.07D;
    }

    public EntityDragonCharge(EntityType<? extends Fireball> type, Level worldIn, double posX,
                              double posY, double posZ, double accelX, double accelY, double accelZ) {
        super(type, posX, posY, posZ, new Vec3(accelX, accelY, accelZ), worldIn);
        this.accelerationPower = 0.07D;
    }

    public EntityDragonCharge(EntityType<? extends Fireball> type, Level worldIn,
                              EntityDragonBase shooter, double accelX, double accelY, double accelZ) {
        super(type, shooter, new Vec3(accelX, accelY, accelZ), worldIn);
        this.accelerationPower = 0.07D;
    }

    @Override
    protected void onHit(@NotNull HitResult movingObject) {
        Entity shootingEntity = this.getOwner();
        if (!this.level().isClientSide()) {
            if (movingObject.getType() == HitResult.Type.ENTITY) {
                Entity entity = ((EntityHitResult) movingObject).getEntity();

                if (entity instanceof IDragonProjectile) {
                    return;
                }
                if (shootingEntity != null && shootingEntity instanceof EntityDragonBase) {
                    EntityDragonBase dragon = (EntityDragonBase) shootingEntity;
                    if (dragon.isAlliedTo(entity) || dragon.is(entity) || dragon.isPart(entity)) {
                        return;
                    }
                }
                if (entity == null || !(entity instanceof IDragonProjectile) && entity != shootingEntity && shootingEntity instanceof EntityDragonBase) {
                    EntityDragonBase dragon = (EntityDragonBase) shootingEntity;
                    if (shootingEntity != null && (entity == shootingEntity || (entity instanceof TamableAnimal && ((EntityDragonBase) shootingEntity).isOwnedBy(((EntityDragonBase) shootingEntity).getOwner())))) {
                        return;
                    }
                    if (dragon != null) {
                        dragon.randomizeAttacks();
                    }
                    this.remove(RemovalReason.DISCARDED);
                }
                if (entity != null && !(entity instanceof IDragonProjectile) && !entity.is(shootingEntity)) {
                    if (shootingEntity != null && (entity.is(shootingEntity) || (shootingEntity instanceof EntityDragonBase && entity instanceof TamableAnimal && ((EntityDragonBase) shootingEntity).getOwner() == ((TamableAnimal) entity).getOwner()))) {
                        return;
                    }
                    if (shootingEntity instanceof EntityDragonBase) {
                        float damageAmount = getDamage() * ((EntityDragonBase) shootingEntity).getDragonStage();

                        EntityDragonBase shootingDragon = (EntityDragonBase) shootingEntity;
                        Entity cause = shootingDragon.getRidingPlayer() != null ? shootingDragon.getRidingPlayer() : shootingDragon;
                        DamageSource source = causeDamage(cause);

                        IafDamage.hurt(entity, source, damageAmount);
                        if (entity instanceof LivingEntity && ((LivingEntity) entity).getHealth() == 0) {
                            ((EntityDragonBase) shootingEntity).randomizeAttacks();
                        }
                    }
                    if (shootingEntity instanceof LivingEntity) {
                        net.minecraft.world.item.enchantment.EnchantmentHelper.doPostAttackEffects(
                            (ServerLevel) this.level(), entity, this.causeDamage(shootingEntity));
                    }
                    this.remove(RemovalReason.DISCARDED);
                }
            }
            if (movingObject.getType() != HitResult.Type.MISS) {
                if (shootingEntity instanceof EntityDragonBase && DragonUtils.canGrief((EntityDragonBase) shootingEntity)) {
                    destroyArea(level(), BlockPos.containing(this.getX(), this.getY(), this.getZ()), ((EntityDragonBase) shootingEntity));
                }
                this.remove(RemovalReason.DISCARDED);
            }
        }

    }

    public abstract DamageSource causeDamage(@Nullable Entity cause);

    public abstract void destroyArea(Level world, BlockPos center, EntityDragonBase destroyer);

    public abstract float getDamage();

    @Override
    public boolean isPickable() {
        return false;
    }

    protected boolean canHitMob(Entity hitMob) {
        Entity shooter = getOwner();
        return hitMob != this && super.canHitEntity(hitMob) && !(shooter == null || hitMob.isAlliedTo(shooter)) && !(hitMob instanceof EntityDragonPart);
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float amount) {
        return false;
    }

    @Override
    public float getPickRadius() {
        return 0F;
    }

}
