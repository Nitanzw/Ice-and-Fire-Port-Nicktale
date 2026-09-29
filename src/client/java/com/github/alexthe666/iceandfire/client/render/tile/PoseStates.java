package com.github.alexthe666.iceandfire.client.render.tile;

import com.github.alexthe666.iceandfire.client.model.DragonEggRenderState;
import com.github.alexthe666.iceandfire.client.model.PixieRenderState;
import com.nicktale.api.client.model.PoseOverride;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

/** Model states that pose the model from a callback (block entities and items have no entity to animate from). */
public final class PoseStates {
    private PoseStates() {
    }

    public static class Generic extends EntityRenderState implements PoseOverride {
        public Runnable pose;

        public Generic() {
        }

        public Generic(Runnable pose) {
            this.pose = pose;
        }

        @Override
        public Runnable poseOverride() {
            return pose;
        }
    }

    public static class Egg extends DragonEggRenderState implements PoseOverride {
        public Runnable pose;

        @Override
        public Runnable poseOverride() {
            return pose;
        }
    }

    public static class Pixie extends PixieRenderState implements PoseOverride {
        public Runnable pose;

        @Override
        public Runnable poseOverride() {
            return pose;
        }
    }
}
