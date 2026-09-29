package com.github.alexthe666.iceandfire.client.particle;

import net.minecraft.client.particle.SingleQuadParticle;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.multiplayer.ClientLevel;

public class ParticleSerpentBubble extends SingleQuadParticle {

    public ParticleSerpentBubble(ClientLevel world, double x, double y, double z, double motX, double motY, double motZ, float size) {
        super(world, x, y, z, motX, motY, motZ, IafParticleSprites.get("sea_serpent_bubble"));
        this.setPos(x, y, z);
        this.quadSize = 0.3F;
    }



    @Override
    public int getLightCoords(float partialTick) {
        //If uncomment : BlockPos needs integers
//        BlockPos blockpos = new BlockPos(this.x, this.y, this.z);
        return 240;
    }

    



    @Override
    public float getQuadSize(float partialTicks) {
        updateFrame(partialTicks);
        return super.getQuadSize(partialTicks);
    }

    private void updateFrame(float partialTicks) {
        
        if (age > this.getLifetime()) {
            this.remove();
        }

            }

    @Override
    protected SingleQuadParticle.Layer getLayer() {
        return SingleQuadParticle.Layer.TRANSLUCENT;
    }
}
