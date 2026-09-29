package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.util.IafEntityUtil;
import com.github.alexthe666.iceandfire.entity.EntityStoneStatue;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class ItemStoneStatue extends Item {

    public ItemStoneStatue() {
        super(IafItemRegistry.itemProperties().stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, net.minecraft.world.item.component.TooltipDisplay display, java.util.function.Consumer<Component> tooltip, @NotNull TooltipFlag flagIn) {
        if (ItemStackData.has(stack)) {
            CompoundTag data = ItemStackData.get(stack);
            boolean isPlayer = data.getBooleanOr("IAFStoneStatuePlayerEntity", false);
            String id = data.getStringOr("IAFStoneStatueEntityID", "");
            if (IafEntityUtil.entityTypeByString(id).orElse(null) != null) {
                EntityType type = IafEntityUtil.entityTypeByString(id).orElse(null);
                MutableComponent untranslated = isPlayer ? Component.translatable("entity.minecraft.player") : Component.translatable(type.getDescriptionId());
                tooltip.accept(untranslated.withStyle(ChatFormatting.GRAY));
            }
        }
    }

    @Override
    public void onCraftedBy(ItemStack itemStack, @NotNull Player player) {
        ItemStackData.update(itemStack, tag -> tag.putBoolean("IAFStoneStatuePlayerEntity", true));
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        if (context.getClickedFace() != Direction.UP) {
            return InteractionResult.FAIL;
        } else {
            ItemStack stack = context.getPlayer().getItemInHand(context.getHand());
            if (ItemStackData.has(stack)) {
                CompoundTag data = ItemStackData.get(stack);
                String id = data.getStringOr("IAFStoneStatueEntityID", "");
                CompoundTag statueNBT = data.getCompound("IAFStoneStatueNBT");
                EntityStoneStatue statue = new EntityStoneStatue(IafEntityRegistry.STONE_STATUE.get(),
                    context.getLevel());
                statue.readAdditionalSaveData(statueNBT);
                statue.setTrappedEntityTypeString(id);
                double d1 = context.getPlayer().getX() - (context.getClickedPos().getX() + 0.5);
                double d2 = context.getPlayer().getZ() - (context.getClickedPos().getZ() + 0.5);
                float yaw = (float) (Mth.atan2(d2, d1) * (180F / (float) Math.PI)) - 90;
                statue.yRotO = yaw;
                statue.setYRot(yaw);
                statue.yHeadRot = yaw;
                statue.yBodyRot = yaw;
                statue.yBodyRotO = yaw;
                statue.snapTo(context.getClickedPos().getX() + 0.5, context.getClickedPos().getY() + 1, context.getClickedPos().getZ() + 0.5, yaw, 0);
                if (!context.getLevel().isClientSide()) {
                    context.getLevel().addFreshEntity(statue);
                    statue.readAdditionalSaveData(data);
                }
                statue.setCrackAmount(0);

                if (!context.getPlayer().isCreative()) {
                    stack.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.SUCCESS;
    }
}
