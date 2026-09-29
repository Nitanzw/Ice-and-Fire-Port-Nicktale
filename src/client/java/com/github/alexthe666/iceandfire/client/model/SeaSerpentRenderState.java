package com.github.alexthe666.iceandfire.client.model;

/** Per-frame snapshot for the sea serpent Tabula animator. */
public class SeaSerpentRenderState extends IafRenderState {
    public float breathProgress;
    public float jumpProgress;
    public float wantJumpProgress;
    public float jumpRot;
    public float prevJumpRot;
    public int swimCycle;
    public double deltaMovementY;
    public boolean jumpingOutOfWater;
    /** Interpolated segment yaw/pitch, indexed 1..4 like {@code EntitySeaSerpent#getPieceYaw}. */
    public final float[] pieceYaw = new float[5];
    public final float[] piecePitch = new float[5];
    public boolean ancient;
    public boolean blinking;
}
