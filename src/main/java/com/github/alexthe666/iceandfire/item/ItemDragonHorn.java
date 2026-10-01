package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.util.IafEntityUtil;
import net.minecraft.world.entity.EntitySpawnReason;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class ItemDragonHorn extends Item {

    public ItemDragonHorn() {
        super((IafItemRegistry.itemProperties())/*.tab(IceAndFire.TAB_ITEMS)*/.stacksTo(1));
    }

    public static int getDragonType(ItemStack stack) {
        if (ItemStackData.has(stack)) {
            String id = ItemStackData.get(stack).getStringOr("DragonHornEntityID", "");
            if (IafEntityUtil.entityTypeByString(id).isPresent()) {
                EntityType entityType = IafEntityUtil.entityTypeByString(id).get();
                if (entityType == IafEntityRegistry.FIRE_DRAGON.get())
                    return 1;

                if (entityType == IafEntityRegistry.ICE_DRAGON.get())
                    return 2;

                if (entityType == IafEntityRegistry.LIGHTNING_DRAGON.get())
                    return 3;
            }
        }

        return 0;
    }


    @Override
    public void onCraftedBy(ItemStack itemStack, @NotNull Player player) {
        super.onCraftedBy(itemStack, player);
    }


    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack stack, Player playerIn, @NotNull LivingEntity target, @NotNull InteractionHand hand) {
        ItemStack trueStack = playerIn.getItemInHand(hand);
        CompoundTag data = ItemStackData.get(trueStack);
        if (!playerIn.level().isClientSide() && hand == InteractionHand.MAIN_HAND && target instanceof EntityDragonBase && ((EntityDragonBase) target).isOwnedBy(playerIn) && data.getCompoundOrEmpty("EntityTag").isEmpty()) {
            CompoundTag newTag = new CompoundTag();

            CompoundTag entityTag = com.nicktale.api.server.entity.EntityDataIO.saveWithoutId(target);
            newTag.put("EntityTag", entityTag);

            newTag.putString("DragonHornEntityID", BuiltInRegistries.ENTITY_TYPE.getKey(target.getType()).toString());
            ItemStackData.set(trueStack, newTag);

            playerIn.swing(hand);
            playerIn.level().playSound(playerIn, playerIn.blockPosition(), SoundEvents.ZOMBIE_VILLAGER_CONVERTED, SoundSource.NEUTRAL, 3.0F, 0.75F);
            target.remove(Entity.RemovalReason.DISCARDED);
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }


    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        if (context.getClickedFace() != Direction.UP)
            return InteractionResult.FAIL;
        ItemStack stack = context.getItemInHand();
        CompoundTag data = ItemStackData.get(stack);
        if (!data.getStringOr("DragonHornEntityID", "").isEmpty()) {
            Level world = context.getLevel();
            String id = data.getStringOr("DragonHornEntityID", "");
            EntityType type = IafEntityUtil.entityTypeByString(id).orElse(null);
            if (type != null) {
                Entity entity = type.create(world, EntitySpawnReason.EVENT);
                if (entity instanceof EntityDragonBase) {
                    EntityDragonBase dragon = (EntityDragonBase) entity;
                    dragon.load(com.nicktale.api.server.entity.EntityDataIO.input(world.registryAccess(), data.getCompoundOrEmpty("EntityTag")));
                }
                //Still needed to allow for intercompatibility
                if (data.contains("EntityUUID"))
                    entity.setUUID(data.read("EntityUUID", net.minecraft.core.UUIDUtil.CODEC).orElseThrow());

                entity.snapTo(context.getClickedPos().getX() + 0.5D, (context.getClickedPos().getY() + 1), context.getClickedPos().getZ() + 0.5D, 180 + (context.getHorizontalDirection()).toYRot(), 0.0F);
                if (world.addFreshEntity(entity)) {
                    data.remove("DragonHornEntityID");
                    data.remove("EntityTag");
                    data.remove("EntityUUID");
                    ItemStackData.set(stack, data);
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, net.minecraft.world.item.component.TooltipDisplay display, java.util.function.Consumer<Component> tooltip, @NotNull TooltipFlag flagIn) {
        if (ItemStackData.has(stack)) {
            CompoundTag data = ItemStackData.get(stack);
            CompoundTag entityTag = data.getCompoundOrEmpty("EntityTag");
            if (!entityTag.isEmpty()) {
                String id = data.getStringOr("DragonHornEntityID", "");
                if (IafEntityUtil.entityTypeByString(id).isPresent()) {
                    EntityType type = IafEntityUtil.entityTypeByString(id).get();
                    tooltip.accept((Component.translatable(type.getDescriptionId())).withStyle(getTextColorForEntityType(type)));
                    String name = (Component.translatable("dragon.unnamed")).getString();
                    java.util.Optional<Component> customName = entityTag.read("CustomName", net.minecraft.network.chat.ComponentSerialization.CODEC);
                    if (customName.isPresent()) {
                        name = customName.get().getString();
                    }

                    tooltip.accept((Component.literal(name)).withStyle(ChatFormatting.GRAY));
                    String gender = (Component.translatable("dragon.gender")).getString() + " " + (Component.translatable(entityTag.getBooleanOr("Gender", false) ? "dragon.gender.male" : "dragon.gender.female")).getString();
                    tooltip.accept((Component.literal(gender)).withStyle(ChatFormatting.GRAY));
                    int stagenumber = entityTag.getIntOr("AgeTicks", 0) / 24000;
                    int stage1 = 0;
                    if (stagenumber >= 100) {
                        stage1 = 5;
                    } else if (stagenumber >= 75) {
                        stage1 = 4;
                    } else if (stagenumber >= 50) {
                        stage1 = 3;
                    } else if (stagenumber >= 25) {
                        stage1 = 2;
                    } else {
                        stage1 = 1;
                    }
                    String stage = (Component.translatable("dragon.stage")).getString() + " " + stage1 + " " + (Component.translatable("dragon.days.front")).getString() + stagenumber + " " + (Component.translatable("dragon.days.back")).getString();
                    tooltip.accept((Component.literal(stage)).withStyle(ChatFormatting.GRAY));
                }
            }

        }
    }

    private ChatFormatting getTextColorForEntityType(EntityType type) {
        if (type == IafEntityRegistry.FIRE_DRAGON.get())
            return ChatFormatting.DARK_RED;

        if (type == IafEntityRegistry.ICE_DRAGON.get())
            return ChatFormatting.BLUE;

        if (type == IafEntityRegistry.LIGHTNING_DRAGON.get())
            return ChatFormatting.DARK_PURPLE;

        return ChatFormatting.GRAY;
    }
}
