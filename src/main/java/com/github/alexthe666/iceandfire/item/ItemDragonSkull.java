package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.entity.EntityDragonSkull;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class ItemDragonSkull extends Item {
    private final int dragonType;

    public ItemDragonSkull(int dragonType) {
        super(IafItemRegistry.itemProperties()/*.tab(IceAndFire.TAB_ITEMS)*/.stacksTo(1));
        this.dragonType = dragonType;
    }

    static String getName(int type) {
        return "dragon_skull_%s".formatted(getType(type));
    }

    private static String getType(int type) {
        if (type == 2) {
            return "lightning";
        } else if (type == 1) {
            return "ice";
        } else {
            return "fire";
        }
    }

    @Override
    public void onCraftedBy(ItemStack itemStack, @NotNull Player player) {
        super.onCraftedBy(itemStack, player);
    }

    @Override
    public void inventoryTick(ItemStack stack, @NotNull net.minecraft.server.level.ServerLevel worldIn, @NotNull Entity entityIn, net.minecraft.world.entity.EquipmentSlot slot) {
        if (!ItemStackData.has(stack)) {
            ItemStackData.update(stack, tag -> {
                tag.putInt("Stage", 4);
                tag.putInt("DragonAge", 75);
            });
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, net.minecraft.world.item.component.TooltipDisplay display, java.util.function.Consumer<Component> tooltip, @NotNull TooltipFlag flagIn) {
        String iceorfire = "dragon." + getType(dragonType);
        tooltip.accept(Component.translatable(iceorfire).withStyle(ChatFormatting.GRAY));
        if (ItemStackData.has(stack)) {
            tooltip.accept(Component.translatable("dragon.stage").withStyle(ChatFormatting.GRAY).append(Component.literal(" " + ItemStackData.get(stack).getInt("Stage"))));
        }
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        ItemStack stack = context.getPlayer().getItemInHand(context.getHand());
        /*
         * EntityDragonEgg egg = new EntityDragonEgg(worldIn);
         * egg.setPosition(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() +
         * 0.5); if(!worldIn.isRemote){ worldIn.spawnEntityInWorld(egg); }
         */
        if (ItemStackData.has(stack)) {
            CompoundTag data = ItemStackData.get(stack);
            EntityDragonSkull skull = new EntityDragonSkull(IafEntityRegistry.DRAGON_SKULL.get(), context.getLevel());
            skull.setDragonType(dragonType);
            skull.setStage(data.getIntOr("Stage", 0));
            skull.setDragonAge(data.getIntOr("DragonAge", 0));
            BlockPos offset = context.getClickedPos().relative(context.getClickedFace(), 1);
            skull.snapTo(offset.getX() + 0.5, offset.getY(), offset.getZ() + 0.5, 0, 0);
            float yaw = context.getPlayer().getYRot();
            if (context.getClickedFace() != Direction.UP) {
                yaw = context.getPlayer().getDirection().toYRot();
            }
            skull.setYaw(yaw);
            if (stack.hasCustomHoverName()) {
                skull.setCustomName(stack.getHoverName());
            }
            if (!context.getLevel().isClientSide()) {
                context.getLevel().addFreshEntity(skull);
            }
            if (!context.getPlayer().isCreative()) {
                stack.shrink(1);
            }
        }
        return InteractionResult.SUCCESS;

    }
}
