package com.github.alexthe666.iceandfire.pathfinding;

import com.github.alexthe666.iceandfire.entity.EntityCyclops;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

/**
 * Ground navigation for cyclopes. Vanilla's walk evaluator accounts for the
 * mob's full bounding box, so this keeps their wide collision footprint in
 * pathfinding without relying on an external collision-navigation library.
 */
public class PathNavigateCyclops extends GroundPathNavigation {

    public PathNavigateCyclops(EntityCyclops cyclops, Level level) {
        super(cyclops, level);
    }

    @Override
    protected PathFinder createPathFinder(int maxVisitedNodes) {
        WalkNodeEvaluator evaluator = new WalkNodeEvaluator();
        evaluator.setCanPassDoors(true);
        evaluator.setCanFloat(true);
        this.nodeEvaluator = evaluator;
        return new PathFinder(evaluator, maxVisitedNodes);
    }
}
