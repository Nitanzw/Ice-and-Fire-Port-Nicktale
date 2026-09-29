package com.github.alexthe666.iceandfire.client.model;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;

/** Render state for non-living entities (projectiles, eggs, statues...) that need rotation and age. */
public class SimpleEntityRenderState extends EntityRenderState {
    public Entity entity;
    public float yRot;
    public float xRot;
    public float partialTick;
}
