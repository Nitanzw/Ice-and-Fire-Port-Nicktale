package com.github.alexthe666.iceandfire.client.render.entity;

import com.nicktale.api.client.model.TabulaModel;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.EntityLightningDragon;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class RenderLightningDragon extends RenderDragonBase {

    public RenderLightningDragon(EntityRendererProvider.Context context, TabulaModel model, int dragonType) {
        super(context, model, dragonType);
    }

    @Override
    public boolean shouldRender(@NotNull EntityDragonBase livingEntityIn, @NotNull Frustum camera, double camX, double camY, double camZ) {
        if (super.shouldRender(livingEntityIn, camera, camX, camY, camZ)) {
            return true;
        } else {
            EntityLightningDragon lightningDragon = (EntityLightningDragon) livingEntityIn;
            if (lightningDragon.hasLightningTarget()) {
                Vec3 Vector3d1 = lightningDragon.getHeadPosition();
                Vec3 Vector3d = new Vec3(lightningDragon.getLightningTargetX(), lightningDragon.getLightningTargetY(), lightningDragon.getLightningTargetZ());
                return camera.isVisible(new AABB(Vector3d1.x, Vector3d1.y, Vector3d1.z, Vector3d.x, Vector3d.y, Vector3d.z));
            }
            return false;
        }
    }

}
