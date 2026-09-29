package com.github.alexthe666.iceandfire.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;

/**
 * First-person ghost scare. The 1.20 version drew the ghost model straight into the buffer source, which the
 * 26.x particle pipeline no longer allows; the scare is shown by {@code ScareOverlay} instead.
 */
public class ParticleGhostAppearance extends Particle {

    public ParticleGhostAppearance(ClientLevel worldIn, double xCoordIn, double yCoordIn, double zCoordIn, int ghost) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn);
        this.gravity = 0.0F;
        this.lifetime = 15;
        com.github.alexthe666.iceandfire.client.gui.ScareOverlay.trigger(15, 0xB8D8FF);
    }

    @Override
    public ParticleRenderType getGroup() {
        return ParticleRenderType.NO_RENDER;
    }
}
