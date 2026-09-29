package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.enums.EnumBestiaryPages;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class ItemBestiary extends Item {

    public ItemBestiary() {
        super(IafItemRegistry.itemProperties()/*.tab(IceAndFire.TAB_ITEMS)*/.stacksTo(1));
    }

    @Override
    public void onCraftedBy(ItemStack stack, Player playerIn) {
        setPageIds(stack, List.of(EnumBestiaryPages.INTRODUCTION.ordinal()));
    }

    @Override
    public InteractionResult use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack itemStackIn = playerIn.getItemInHand(handIn);
        if (worldIn.isClientSide()) {
            IceAndFire.PROXY.openBestiaryGui(itemStackIn);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void inventoryTick(ItemStack stack, net.minecraft.server.level.ServerLevel worldIn, net.minecraft.world.entity.Entity entityIn, net.minecraft.world.entity.EquipmentSlot slot) {
        if (!ItemStackData.contains(stack, "Pages")) {
            setPageIds(stack, List.of(EnumBestiaryPages.INTRODUCTION.ordinal()));
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flagIn) {
        if (ItemStackData.has(stack)) {
            if (IceAndFire.PROXY.shouldSeeBestiaryContents()) {
                tooltip.accept(Component.translatable("bestiary.contains").withStyle(ChatFormatting.GRAY));
                final Set<EnumBestiaryPages> pages = EnumBestiaryPages.containedPages(getPageIds(stack));
                for (EnumBestiaryPages page : pages) {
                    tooltip.accept(Component.literal(ChatFormatting.WHITE + "-").append(Component.translatable("bestiary." + page.name().toLowerCase())).withStyle(ChatFormatting.GRAY));
                }
            } else {
                tooltip.accept(Component.translatable("bestiary.hold_shift").withStyle(ChatFormatting.GRAY));
            }
        }
    }

    public static List<Integer> getPageIds(ItemStack stack) {
        return java.util.Arrays.stream(ItemStackData.get(stack).getIntArray("Pages").orElseGet(() -> new int[0])).boxed().toList();
    }

    public static void setPageIds(ItemStack stack, Collection<Integer> pageIds) {
        ItemStackData.update(stack, tag -> tag.putIntArray("Pages", pageIds.stream().mapToInt(Integer::intValue).toArray()));
    }
}
