package com.nicktale.api.animation;

/**
 * A timed animation clip. Only the duration lives here; the pose is defined by the entity model.
 * Instances are compared by identity, so keep them in static fields.
 */
public final class Animation {
    private final int duration;

    private Animation(int duration) {
        this.duration = duration;
    }

    public static Animation create(int duration) {
        return new Animation(duration);
    }

    /** Length of the clip in ticks. */
    public int getDuration() {
        return duration;
    }
}
