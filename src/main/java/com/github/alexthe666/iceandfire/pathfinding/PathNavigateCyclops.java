package com.github.alexthe666.iceandfire.pathfinding;

import com.github.alexthe666.iceandfire.entity.EntityCyclops;
import com.nicktale.api.server.entity.collision.CustomCollisionsNavigator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

/**
 * Ground navigation for cyclopes using Nicktale API's collision-aware navigator
 * base and vanilla's full-bounding-box walk evaluator.
 */
public class PathNavigateCyclops extends CustomCollisionsNavigator {

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
