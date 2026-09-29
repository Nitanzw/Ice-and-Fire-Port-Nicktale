package com.github.alexthe666.iceandfire.entity;

import com.nicktale.api.animation.Animation;

/** Hippogryph clips, shared by the entity (server) and its model (client). */
public final class HippogryphAnimations {
    public static final Animation EAT = Animation.create(25);
    public static final Animation SPEAK = Animation.create(15);
    public static final Animation SCRATCH = Animation.create(25);
    public static final Animation BITE = Animation.create(20);

    private HippogryphAnimations() {
    }
}
