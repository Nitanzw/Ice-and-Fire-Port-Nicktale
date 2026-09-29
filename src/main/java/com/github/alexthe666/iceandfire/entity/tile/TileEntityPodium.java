package com.github.alexthe666.iceandfire.entity.tile;

import com.github.alexthe666.iceandfire.IceAndFire;
import com.github.alexthe666.iceandfire.inventory.ContainerPodium;
import com.github.alexthe666.iceandfire.item.ItemDragonEgg;
import com.github.alexthe666.iceandfire.item.ItemMyrmexEgg;
import com.github.alexthe666.iceandfire.message.MessageUpdatePodium;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;


public class TileEntityPodium extends BaseContainerBlockEntity implements WorldlyContainer {

    private static final int[] slotsTop = new int[]{0};
    public int ticksExisted;
    public int prevTicksExisted;
    private NonNullList<ItemStack> stacks = NonNullList.withSize(1, ItemStack.EMPTY);

    public TileEntityPodium(BlockPos pos, BlockState state) {
        super(IafTileEntityRegistry.PODIUM.get(), pos, state);
    }

    //TODO: This must be easier to do
    public static void tick(Level level, BlockPos pos, BlockState state, TileEntityPodium entityPodium) {
        entityPodium.prevTicksExisted = entityPodium.ticksExisted;
        entityPodium.ticksExisted++;
    }

    public net.minecraft.world.phys.AABB getRenderBoundingBox() {
        return new net.minecraft.world.phys.AABB(
            worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(),
            worldPosition.getX() + 1, worldPosition.getY() + 3, worldPosition.getZ() + 1);
    }

    @Override
    public int getContainerSize() {
        return this.stacks.size();
    }

    @Override
    public @NotNull ItemStack getItem(int index) {
        return this.stacks.get(index);
    }

    @Override
    public @NotNull ItemStack removeItem(int index, int count) {
        if (!this.stacks.get(index).isEmpty()) {
            ItemStack itemstack;

            if (this.stacks.get(index).getCount() <= count) {
                itemstack = this.stacks.get(index);
                this.stacks.set(index, ItemStack.EMPTY);
                return itemstack;
            } else {
                itemstack = this.stacks.get(index).split(count);

                if (this.stacks.get(index).isEmpty()) {
                    this.stacks.set(index, ItemStack.EMPTY);
                }

                return itemstack;
            }
        } else {
            return ItemStack.EMPTY;
        }
    }

    public ItemStack getStackInSlotOnClosing(int index) {
        if (!this.stacks.get(index).isEmpty()) {
            ItemStack itemstack = this.stacks.get(index);
            this.stacks.set(index, itemstack);
            return itemstack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void setItem(int index, @NotNull ItemStack stack) {
        this.stacks.set(index, stack);

        if (!stack.isEmpty() && stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }
        this.setChanged();
        if (!level.isClientSide()) {
            IceAndFire.sendMSGToAll(new MessageUpdatePodium(this.getBlockPos().asLong(), stacks.get(0)));
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.stacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(input, this.stacks);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, this.stacks);
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, @NotNull ItemStack stack, Direction direction) {
        return index != 0 || (stack.getItem() instanceof ItemDragonEgg || stack.getItem() instanceof ItemMyrmexEgg);
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        this.stacks.clear();
    }

    @Override
    public int @NotNull [] getSlotsForFace(@NotNull Direction side) {
        return slotsTop;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, @NotNull ItemStack stack, @NotNull Direction direction) {
        return false;
    }

    @Override
    public boolean canPlaceItem(int index, @NotNull ItemStack stack) {
        return false;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull net.minecraft.nbt.CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveCustomOnly(registries);
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return getDefaultName();
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("block.iceandfire.podium");
    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int id, @NotNull Inventory player) {
        return new ContainerPodium(id, this, player, new SimpleContainerData(0));
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < this.getContainerSize(); i++) {
            if (!this.getItem(i).isEmpty())
                return false;
        }
        return true;
    }

    @Override
    protected NonNullList<ItemStack> getItems() {
        return this.stacks;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> items) {
        this.stacks = items;
    }
}
