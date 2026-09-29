package com.github.alexthe666.iceandfire.entity.tile;

import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStackResourceHandler;

/** Exposes the jar's one-item pixie dust output through NeoForge's transactional item capability. */
public final class PixieJarInvWrapper extends ItemStackResourceHandler {
    private final TileEntityJar jar;

    public PixieJarInvWrapper(TileEntityJar jar) {
        this.jar = jar;
    }

    @Override
    protected ItemStack getStack() {
        return this.jar.hasProduced ? new ItemStack(IafItemRegistry.PIXIE_DUST.get()) : ItemStack.EMPTY;
    }

    @Override
    protected void setStack(ItemStack stack) {
        this.jar.hasProduced = !stack.isEmpty();
    }

    @Override
    protected boolean isValid(ItemResource resource) {
        return false;
    }

    @Override
    protected int getCapacity(ItemResource resource) {
        return resource.isEmpty() ? 1 : 0;
    }

    @Override
    protected void onRootCommit(ItemStack originalState) {
        this.jar.setChanged();
    }
}
