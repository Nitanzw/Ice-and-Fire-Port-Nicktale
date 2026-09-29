package com.github.alexthe666.iceandfire.client.model;

/** Data the models need from their entity, copied once per frame by the renderer. */
public class MyrmexRenderState extends IafRenderState {
    public float flyProgress;
    public boolean hasPassengers;
    public float hidingProgress;
    public float holdingProgress;
    public boolean isFlying;
    public boolean isHiding;
    public boolean onGround;
    public int tickCount;
}
