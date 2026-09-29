package com.github.alexthe666.iceandfire.item;

import net.minecraft.server.level.ServerLevel;
import com.github.alexthe666.iceandfire.entity.EntityTideTrident;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Weapon;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class ItemTideTrident extends TridentItem {

    public ItemTideTrident() {
        super(IafItemRegistry.itemProperties().durability(400).attributes(createAttributes())
            .component(DataComponents.TOOL, TridentItem.createToolProperties())
            .component(DataComponents.WEAPON, new Weapon(1))
            .enchantable(1));
    }

    private static ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
            .add(Attributes.ATTACK_DAMAGE,
                new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, 12.0D, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND)
            .add(Attributes.ATTACK_SPEED,
                new AttributeModifier(Item.BASE_ATTACK_SPEED_ID, -2.9D, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND)
            .build();
    }

    @Override
    public boolean releaseUsing(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity user, int timeLeft) {
        if (!(user instanceof Player player) || this.getUseDuration(stack, player) - timeLeft < 10) {
            return false;
        }

        float riptideStrength = EnchantmentHelper.getTridentSpinAttackStrength(stack, player);
        if (riptideStrength > 0 && !player.isInWaterOrRain()) {
            return false;
        }

        if (level instanceof net.minecraft.server.level.ServerLevel) {
            stack.hurtAndBreak(1, player, player.getUsedItemHand());
            if (riptideStrength == 0) {
                EntityTideTrident thrown = new EntityTideTrident(level, player, stack);
                thrown.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, TridentItem.PROJECTILE_SHOOT_POWER, 1.0F);
                if (player.getAbilities().instabuild) {
                    thrown.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                }

                level.addFreshEntity(thrown);
                level.playSound(null, thrown, SoundEvents.TRIDENT_THROW, SoundSource.PLAYERS, 1.0F, 1.0F);
                if (!player.getAbilities().instabuild) {
                    player.getInventory().removeItem(stack);
                }
                player.awardStat(Stats.ITEM_USED.get(this));
                return true;
            }
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        if (riptideStrength > 0) {
            Vec3 direction = player.getViewVector(1.0F).normalize().scale(riptideStrength);
            player.push(direction.x, direction.y, direction.z);
            player.startAutoSpinAttack(20, TridentItem.BASE_DAMAGE, stack);
            if (player.onGround()) {
                player.move(MoverType.SELF, new Vec3(0.0D, 1.1999999D, 0.0D));
            }

            Holder<SoundEvent> sound = EnchantmentHelper.pickHighestLevel(stack, EnchantmentEffectComponents.TRIDENT_SOUND)
                .orElse(SoundEvents.TRIDENT_RIPTIDE_1);
            level.playSound(null, player, sound.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
            return true;
        }

        return false;
    }

    private static boolean isTridentEnchantment(Holder<Enchantment> enchantment) {
        return enchantment.is(Enchantments.LOYALTY)
            || enchantment.is(Enchantments.IMPALING)
            || enchantment.is(Enchantments.RIPTIDE)
            || enchantment.is(Enchantments.CHANNELING)
            || enchantment.is(Enchantments.PIERCING);
    }

    @Override
    public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
        return isTridentEnchantment(enchantment) || super.supportsEnchantment(stack, enchantment);
    }

    @Override
    public boolean isPrimaryItemFor(ItemStack stack, Holder<Enchantment> enchantment) {
        return isTridentEnchantment(enchantment) || super.isPrimaryItemFor(stack, enchantment);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, net.minecraft.world.item.component.TooltipDisplay display, java.util.function.Consumer<Component> tooltip, TooltipFlag flagIn) {

        tooltip.accept(Component.translatable("item.iceandfire.legendary_weapon.desc").withStyle(ChatFormatting.GRAY));
        tooltip.accept(Component.translatable("item.iceandfire.tide_trident.desc_0").withStyle(ChatFormatting.GRAY));
        tooltip.accept(Component.translatable("item.iceandfire.tide_trident.desc_1").withStyle(ChatFormatting.GRAY));
    }
}
