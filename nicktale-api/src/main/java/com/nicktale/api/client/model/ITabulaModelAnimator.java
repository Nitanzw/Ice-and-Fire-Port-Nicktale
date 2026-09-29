// SPDX-License-Identifier: LGPL-3.0-or-later
package com.nicktale.api.client.model;

/** Applies procedural animation to a Tabula model for one render frame. */
@FunctionalInterface
public interface ITabulaModelAnimator<T> {
    void setRotationAngles(TabulaModel model, T state, float limbSwing, float limbSwingAmount,
                           float ageInTicks, float rotationYaw, float rotationPitch, float scale);
}
