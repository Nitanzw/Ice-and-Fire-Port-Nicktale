package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.util.IafEntityUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

import java.util.EnumSet;
import java.util.function.Predicate;

public class CyclopsAITargetSheepPlayers<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {

    public CyclopsAITargetSheepPlayers(Mob goalOwnerIn, Class<T> targetClassIn, boolean checkSight) {
        super(goalOwnerIn, targetClassIn, 0, checkSight, true, IafEntityUtil.selector(new Predicate<LivingEntity>() {
            @Override
            public boolean test(LivingEntity livingEntity) {
                return false; //TODO Sheep hunt cyclops
            }
        }));
        this.setFlags(EnumSet.of(Flag.TARGET));

    }


}
