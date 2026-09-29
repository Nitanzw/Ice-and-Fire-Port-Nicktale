package com.github.alexthe666.iceandfire.client.particle;

import net.minecraft.client.particle.SingleQuadParticle;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.multiplayer.ClientLevel;

public class ParticleDreadTorch extends SingleQuadParticle {

    private final boolean big;

    public ParticleDreadTorch(ClientLevel world, double x, double y, double z, double motX, double motY, double motZ, float size) {
        super(world, x, y, z, motX, motY, motZ, IafParticleSprites.get("snowflake_0"));
        this.setPos(x, y, z);
        this.yd += 0.01D;
        big = random.nextBoolean();
        this.sprite = IafParticleSprites.get(big ? "snowflake_1" : "snowflake_0");
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
