package com.github.alexthe666.iceandfire.client.model;

import com.github.alexthe666.iceandfire.client.model.IFChainBuffer;

/** Data the models need from their entity, copied once per frame by the renderer. */
public class AmphithereRenderState extends IafRenderState {
    public float diveProgress;
    public float flapProgress;
    public float groundProgress;
    public boolean onGround;
    public IFChainBuffer pitch_buffer;
    public IFChainBuffer roll_buffer;
    public float sitProgress;
    public IFChainBuffer tail_buffer;
}
