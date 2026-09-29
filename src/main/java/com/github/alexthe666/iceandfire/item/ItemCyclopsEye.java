package com.github.alexthe666.iceandfire.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class ItemCyclopsEye extends Item {

    public ItemCyclopsEye() {
        super(IafItemRegistry.itemProperties()/*.tab(IceAndFire.TAB_ITEMS)*/.durability(500));
    }

    @Override
    public void inventoryTick(ItemStack stack, @NotNull ServerLevel world, @NotNull Entity entity, EquipmentSlot slot) {
        if (!(entity instanceof LivingEntity living)) {
            return;
        }

        if (living.getMainHandItem() == stack || living.getOffhandItem() == stack) {
            double range = 15;
            boolean inflictedDamage = false;
            for (Mob mob : world.getEntitiesOfClass(Mob.class, new AABB(living.getX() - range, living.getY() - range, living.getZ() - range, living.getX() + range, living.getY() + range, living.getZ() + range))) {
                if (!mob.is(living) && !mob.isAlliedTo(living) && (mob.getTarget() == living || mob.getLastHurtByMob() == living || mob instanceof Enemy)) {
                    mob.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 10, 1));
                    inflictedDamage = true;
                }
            }
            if (inflictedDamage) {
                ItemStackData.update(stack, tag -> tag.putInt("HurtingTicks", tag.getIntOr("HurtingTicks", 0) + 1));
            }
        }
        if (ItemStackData.get(stack).getIntOr("HurtingTicks", 0) > 120) {
            EquipmentSlot hand = living.getMainHandItem() == stack ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
            stack.hurtAndBreak(1, living, hand);
            ItemStackData.update(stack, tag -> tag.putInt("HurtingTicks", 0));
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.TooltipContext context, net.minecraft.world.item.component.TooltipDisplay display, java.util.function.Consumer<Component> tooltip, @NotNull TooltipFlag flagIn) {
        tooltip.accept(Component.translatable("item.iceandfire.legendary_weapon.desc").withStyle(ChatFormatting.GRAY));
        tooltip.accept(Component.translatable("item.iceandfire.cyclops_eye.desc_0").withStyle(ChatFormatting.GRAY));
        tooltip.accept(Component.translatable("item.iceandfire.cyclops_eye.desc_1").withStyle(ChatFormatting.GRAY));
    }
}
