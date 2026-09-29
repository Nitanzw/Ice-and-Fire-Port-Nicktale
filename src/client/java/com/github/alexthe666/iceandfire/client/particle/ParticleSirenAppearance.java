package com.github.alexthe666.iceandfire.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;

/** First-person siren scare; see {@link ParticleGhostAppearance} for why it does not draw yet. */
public class ParticleSirenAppearance extends Particle {

    public ParticleSirenAppearance(ClientLevel worldIn, double xCoordIn, double yCoordIn, double zCoordIn, int sirenType) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn);
        this.gravity = 0.0F;
        this.lifetime = 30;
        com.github.alexthe666.iceandfire.client.gui.ScareOverlay.trigger(30, 0x6A3FA0);
    }

    @Override
    public ParticleRenderType getGroup() {
        return ParticleRenderType.NO_RENDER;
    }
}
