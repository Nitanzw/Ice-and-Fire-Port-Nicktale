package com.github.alexthe666.iceandfire.client.model;

import com.github.alexthe666.iceandfire.entity.util.ChainBuffer;

/** Data the models need from their entity, copied once per frame by the renderer. */
public class AmphithereRenderState extends IafRenderState {
    public float diveProgress;
    public float flapProgress;
    public float groundProgress;
    public boolean onGround;
    public ChainBuffer pitch_buffer;
    public ChainBuffer roll_buffer;
    public float sitProgress;
    public ChainBuffer tail_buffer;
}
