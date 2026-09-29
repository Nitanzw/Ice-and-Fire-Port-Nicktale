package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/** The Tide Trident uses the vanilla 26.2 trident projectile behavior and payload item. */
public class EntityTideTrident extends ThrownTrident {
    public EntityTideTrident(EntityType<? extends EntityTideTrident> type, Level level) {
        super(type, level);
        this.setPickupItemStack(new ItemStack(IafItemRegistry.TIDE_TRIDENT.get()));
        this.setBaseDamage(12.0D);
    }

    public EntityTideTrident(Level level, LivingEntity thrower, ItemStack thrownStack) {
        this(IafEntityRegistry.TIDE_TRIDENT.get(), level);
        this.setPos(thrower.getX(), thrower.getEyeY() - 0.1F, thrower.getZ());
        this.setOwner(thrower);
        this.setPickupItemStack(thrownStack.copy());
    }
}
