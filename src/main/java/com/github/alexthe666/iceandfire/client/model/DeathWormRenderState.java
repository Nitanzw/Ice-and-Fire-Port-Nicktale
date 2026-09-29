package com.github.alexthe666.iceandfire.client.model;

import com.github.alexthe666.iceandfire.entity.util.ChainBuffer;

/** Data the models need from their entity, copied once per frame by the renderer. */
public class DeathWormRenderState extends IafRenderState {
    public int getWormJumping;
    public float jumpProgress;
    public float prevJumpProgress;
    public ChainBuffer tail_buffer;
    public int tickCount;
}
