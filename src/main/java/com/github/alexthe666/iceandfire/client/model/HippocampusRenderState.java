package com.github.alexthe666.iceandfire.client.model;

import com.github.alexthe666.iceandfire.entity.util.ChainBuffer;

/** Data the models need from their entity, copied once per frame by the renderer. */
public class HippocampusRenderState extends IafRenderState {
    public boolean onGround;
    public float onLandProgress;
    public float sitProgress;
    public ChainBuffer tail_buffer;
}
