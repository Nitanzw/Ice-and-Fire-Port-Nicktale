// SPDX-License-Identifier: LGPL-3.0-or-later
package com.nicktale.api.client.model;

/**
 * Implemented by a render state that wants to pose the model itself instead of running the model's normal animation,
 * e.g. block-entity and item renderers. The returned action runs when the model is set up for drawing.
 */
public interface PoseOverride {
    Runnable poseOverride();
}
