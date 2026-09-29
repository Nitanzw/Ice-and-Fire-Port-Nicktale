package com.github.alexthe666.iceandfire.mixin;

import com.nicktale.api.server.entity.collision.ICustomCollisions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** {@code Entity#collide} is private, so entities with custom collisions (death worms) hook it here. */
@Mixin(Entity.class)
public abstract class EntityCollideMixin {
    @Inject(method = "collide", at = @At("HEAD"), cancellable = true)
    private void iaf$customCollide(Vec3 movement, CallbackInfoReturnable<Vec3> cir) {
        if ((Object) this instanceof ICustomCollisions) {
            cir.setReturnValue(ICustomCollisions.getAllowedMovementForEntity((Entity) (Object) this, movement));
        }
    }
}
