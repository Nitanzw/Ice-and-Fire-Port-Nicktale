package com.github.alexthe666.iceandfire.client.model;

/** Data the models need from their entity, copied once per frame by the renderer. */
public class PixieRenderState extends IafRenderState {
    public boolean isPixieSitting;
    public net.minecraft.world.item.ItemStack heldItem = net.minecraft.world.item.ItemStack.EMPTY;
    public final net.minecraft.client.renderer.item.ItemStackRenderState heldItemState = new net.minecraft.client.renderer.item.ItemStackRenderState();
}
