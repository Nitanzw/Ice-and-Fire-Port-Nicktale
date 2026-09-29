package com.github.alexthe666.iceandfire.client.model;

/** Data the models need from their entity, copied once per frame by the renderer. */
public class HydraRenderState extends IafRenderState {
    public boolean stoneMob;
    public float[] breathProgress;
    public int getSeveredHead;
    public boolean isAlive;
    public float[] prevBreathProgress;
    public float[] prevSpeakingProgress;
    public float[] prevStrikeProgress;
    public float[] speakingProgress;
    public float[] strikingProgress;
}
