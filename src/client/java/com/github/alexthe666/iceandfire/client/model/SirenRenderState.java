package com.github.alexthe666.iceandfire.client.model;

import com.github.alexthe666.iceandfire.entity.util.ChainBuffer;

/** Data the models need from their entity, copied once per frame by the renderer. */
public class SirenRenderState extends IafRenderState {
    public int getSingingPose;
    public boolean isSinging;
    public boolean isSwimming;
    public boolean onGround;
    public float singProgress;
    public float swimProgress;
    public ChainBuffer tail_buffer;
}
