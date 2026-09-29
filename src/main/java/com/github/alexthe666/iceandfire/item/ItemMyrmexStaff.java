package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import com.github.alexthe666.iceandfire.message.MessageGetMyrmexHive;
import com.github.alexthe666.iceandfire.message.MessageSetMyrmexHiveNull;
import com.github.alexthe666.iceandfire.world.MyrmexWorldData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ItemMyrmexStaff extends Item {

    public ItemMyrmexStaff(boolean jungle) {
        super(IafItemRegistry.itemProperties().stacksTo(1));
    }

    @Override
    public void onCraftedBy(ItemStack itemStack, @NotNull Player player) {
        super.onCraftedBy(itemStack, player);
        initializeHiveData(itemStack);
    }

    @Override
    public void inventoryTick(ItemStack stack, @NotNull net.minecraft.server.level.ServerLevel world, @NotNull Entity entity, net.minecraft.world.entity.EquipmentSlot slot) {
        initializeHiveData(stack);
    }

    private static void initializeHiveData(ItemStack stack) {
        if (!ItemStackData.contains(stack, "HiveUUID")) {
            ItemStackData.update(stack, tag -> tag.putUUID("HiveUUID", new UUID(0, 0)));
        }
    }

    @Override
    public @NotNull InteractionResult use(@NotNull Level worldIn, Player playerIn, @NotNull InteractionHand hand) {
        ItemStack itemStackIn = playerIn.getItemInHand(hand);
        if (playerIn.isShiftKeyDown()) {
            return super.use(worldIn, playerIn, hand);
        }
        CompoundTag data = ItemStackData.get(itemStackIn);
        if (data.hasUUID("HiveUUID")) {
            UUID id = data.getUUID("HiveUUID");
            if (!worldIn.isClientSide()) {
                MyrmexHive hive = MyrmexWorldData.get(worldIn).getHiveFromUUID(id);
                MyrmexWorldData.addHive(worldIn, new MyrmexHive());
                if (hive != null) {
                    IceAndFire.sendMSGToAll(new MessageGetMyrmexHive(hive.toNBT()));
                } else {
                    IceAndFire.sendMSGToAll(new MessageSetMyrmexHiveNull());
                }
            } else if (id != null && !id.equals(new UUID(0, 0))) {
                IceAndFire.PROXY.openMyrmexStaffGui(itemStackIn);
            }
        }
        playerIn.swing(hand);
        return InteractionResult.PASS;
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null) {
            return InteractionResult.PASS;
        }
        if (!player.isShiftKeyDown()) {
            return super.useOn(context);
        } else {
            CompoundTag data = ItemStackData.get(player.getItemInHand(context.getHand()));
            if (data.hasUUID("HiveUUID")) {
                UUID id = data.getUUID("HiveUUID");
                if (!context.getLevel().isClientSide()) {
                    MyrmexHive hive = MyrmexWorldData.get(context.getLevel()).getHiveFromUUID(id);
                    if (hive != null) {
                        IceAndFire.sendMSGToAll(new MessageGetMyrmexHive(hive.toNBT()));
                    } else {
                        IceAndFire.sendMSGToAll(new MessageSetMyrmexHiveNull());
                    }
                } else if (id != null && !id.equals(new UUID(0, 0))) {
                    IceAndFire.PROXY.openMyrmexAddRoomGui(player.getItemInHand(context.getHand()), context.getClickedPos(), player.getDirection());
                }
            }
            player.swing(context.getHand());
            return InteractionResult.SUCCESS;
        }
    }
}
