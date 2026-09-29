package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import com.github.alexthe666.iceandfire.entity.props.EntityDataProvider;
import com.nicktale.api.server.item.CustomToolMaterial;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

/** Shared material abilities for tools whose stats now live in vanilla 26.2 data components. */
public interface DragonSteelOverrides<T extends Item> {
    CustomToolMaterial getToolMaterial();

    float getBaseAttackDamage();

    default float getAttackDamage(T item) {
        return getToolMaterial().getAttackDamageBonus() + getBaseAttackDamage();
    }

    default boolean isDragonsteel(CustomToolMaterial material) {
        return material == DragonSteelTier.DRAGONSTEEL_TIER_FIRE
            || material == DragonSteelTier.DRAGONSTEEL_TIER_ICE
            || material == DragonSteelTier.DRAGONSTEEL_TIER_LIGHTNING
            || material == DragonSteelTier.DRAGONSTEEL_TIER_DREAD_QUEEN;
    }

    default boolean isDragonsteelFire(CustomToolMaterial material) {
        return material == DragonSteelTier.DRAGONSTEEL_TIER_FIRE;
    }

    default boolean isDragonsteelIce(CustomToolMaterial material) {
        return material == DragonSteelTier.DRAGONSTEEL_TIER_ICE;
    }

    default boolean isDragonsteelLightning(CustomToolMaterial material) {
        return material == DragonSteelTier.DRAGONSTEEL_TIER_LIGHTNING;
    }

    default void applyMaterialHit(T item, ItemStack stack, LivingEntity target, LivingEntity attacker) {
        CustomToolMaterial material = getToolMaterial();
        if (material == IafItemRegistry.SILVER_TOOL_MATERIAL && target.getType().builtInRegistryHolder().is(EntityTypeTags.UNDEAD)) {
            target.hurt(attacker.level().damageSources().magic(), getAttackDamage(item) + 3.0F);
        }

        if (material == IafItemRegistry.MYRMEX_CHITIN_TOOL_MATERIAL) {
            if (!target.getType().builtInRegistryHolder().is(EntityTypeTags.ARTHROPOD) || target instanceof EntityDeathWorm) {
                target.hurt(attacker.level().damageSources().generic(), getAttackDamage(item) + 5.0F);
            }
        }

        if (isDragonsteelFire(material) && IafConfig.dragonWeaponFireAbility) {
            target.setSecondsOnFire(15);
            target.knockback(1.0F, attacker.getX() - target.getX(), attacker.getZ() - target.getZ());
        }
        if (isDragonsteelIce(material) && IafConfig.dragonWeaponIceAbility) {
            EntityDataProvider.getCapability(target).ifPresent(data -> data.frozenData.setFrozen(target, 300));
            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 300, 2));
            target.knockback(1.0F, attacker.getX() - target.getX(), attacker.getZ() - target.getZ());
        }
        if (isDragonsteelLightning(material) && IafConfig.dragonWeaponLightningAbility) {
            boolean createLightning = !(attacker instanceof Player) || attacker.attackAnim <= 0.2F;
            if (!attacker.level().isClientSide() && createLightning) {
                LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(target.level());
                if (bolt != null) {
                    bolt.setCause(attacker instanceof Player player ? player : null);
                    bolt.setPos(target.position());
                    target.level().addFreshEntity(bolt);
                }
            }
            target.knockback(1.0F, attacker.getX() - target.getX(), attacker.getZ() - target.getZ());
        }
    }

    default void appendMaterialTooltip(CustomToolMaterial material, ItemStack stack, Item.TooltipContext context,
                                       TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        if (material == IafItemRegistry.SILVER_TOOL_MATERIAL) {
            tooltip.accept(Component.translatable("silvertools.hurt").withStyle(ChatFormatting.GREEN));
        }
        if (material == IafItemRegistry.MYRMEX_CHITIN_TOOL_MATERIAL) {
            tooltip.accept(Component.translatable("myrmextools.hurt").withStyle(ChatFormatting.GREEN));
        }
        if (isDragonsteelFire(material) && IafConfig.dragonWeaponFireAbility) {
            tooltip.accept(Component.translatable("dragon_sword_fire.hurt2").withStyle(ChatFormatting.DARK_RED));
        }
        if (isDragonsteelIce(material) && IafConfig.dragonWeaponIceAbility) {
            tooltip.accept(Component.translatable("dragon_sword_ice.hurt2").withStyle(ChatFormatting.AQUA));
        }
        if (isDragonsteelLightning(material) && IafConfig.dragonWeaponLightningAbility) {
            tooltip.accept(Component.translatable("dragon_sword_lightning.hurt2").withStyle(ChatFormatting.DARK_PURPLE));
        }
    }
}
