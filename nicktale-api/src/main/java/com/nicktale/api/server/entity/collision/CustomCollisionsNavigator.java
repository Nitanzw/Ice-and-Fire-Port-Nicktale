package com.nicktale.api.server.entity.collision;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;

/** Ground navigation base for entities with {@link ICustomCollisions}; subclasses supply the node evaluator. */
public class CustomCollisionsNavigator extends GroundPathNavigation {
    public CustomCollisionsNavigator(Mob mob, Level level) {
        super(mob, level);
    }
}
