package com.github.alexthe666.iceandfire.client.particle;

import net.minecraft.client.particle.SingleQuadParticle;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.multiplayer.ClientLevel;

public class ParticleBlood extends SingleQuadParticle {

    public ParticleBlood(ClientLevel world, double x, double y, double z) {
        super(world, x, y, z, 0, Math.random() * (double) 0.2F + 0.1, 0, IafParticleSprites.get("blood"));
        this.setPos(x, y, z);
        this.yd += 0.01D;
    }




    @Override
    public int getLightCoords(float partialTick) {
        return 240;
    }

    



    @Override
    public float getQuadSize(float partialTicks) {
        updateFrame(partialTicks);
        return super.getQuadSize(partialTicks);
    }

    private void updateFrame(float partialTicks) {
        
        quadSize = 0.125F * (this.lifetime - (this.age));
        quadSize = quadSize * 0.09F;
        xd *= 0.75D;
        yd *= 0.75D;
        zd *= 0.75D;
        if (age > this.getLifetime()) {
            this.remove();
        }

            }

    @Override
    protected SingleQuadParticle.Layer getLayer() {
        return SingleQuadParticle.Layer.TRANSLUCENT;
    }
}
