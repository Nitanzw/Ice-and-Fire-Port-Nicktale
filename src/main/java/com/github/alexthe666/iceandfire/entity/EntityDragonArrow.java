package com.github.alexthe666.iceandfire.entity;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class EntityDragonArrow extends AbstractArrow {

    public EntityDragonArrow(EntityType<? extends AbstractArrow> typeIn, Level worldIn) {
        super(typeIn, worldIn);
        this.setBaseDamage(10);
    }

    public EntityDragonArrow(EntityType<? extends AbstractArrow> typeIn, double x, double y, double z,
                             Level world) {
        super(typeIn, x, y, z, world, new ItemStack(IafItemRegistry.DRAGONBONE_ARROW.get()), null);
        this.setBaseDamage(10);
    }



    public EntityDragonArrow(EntityType<? extends AbstractArrow> typeIn, LivingEntity shooter, Level worldIn) {
        super(typeIn, shooter, worldIn, new ItemStack(IafItemRegistry.DRAGONBONE_ARROW.get()), null);
        this.setBaseDamage(10.0F);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putDouble("damage", 10);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.setBaseDamage(input.getDoubleOr("damage", 10.0));
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(IafItemRegistry.DRAGONBONE_ARROW.get());
    }

}