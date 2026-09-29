// SPDX-License-Identifier: LGPL-3.0-or-later
package com.nicktale.api.client.model.container;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** Immutable decoded form of a Tabula {@code model.json}. */
public final class TabulaModelContainer {
    private final String modelName;
    private final int textureWidth;
    private final int textureHeight;
    private final List<Cube> roots;

    public TabulaModelContainer(String modelName, int textureWidth, int textureHeight, List<Cube> roots) {
        this.modelName = Objects.requireNonNull(modelName, "modelName");
        if (textureWidth <= 0 || textureHeight <= 0) {
            throw new IllegalArgumentException("texture dimensions must be positive");
        }
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.roots = List.copyOf(roots);
    }

    public String getModelName() {
        return modelName;
    }

    public int getTextureWidth() {
        return textureWidth;
    }

    public int getTextureHeight() {
        return textureHeight;
    }

    public List<Cube> getRootCubes() {
        return roots;
    }

    public record Cube(String name, float[] dimensions, float[] position, float[] offset,
                       float[] rotation, float[] scale, int[] textureOffset, boolean mirror,
                       float inflate, boolean hidden, List<Cube> children) {
        public Cube {
            Objects.requireNonNull(name, "name");
            dimensions = dimensions.clone();
            position = position.clone();
            offset = offset.clone();
            rotation = rotation.clone();
            scale = scale.clone();
            textureOffset = textureOffset.clone();
            children = List.copyOf(children);
        }

        @Override public float[] dimensions() { return dimensions.clone(); }
        @Override public float[] position() { return position.clone(); }
        @Override public float[] offset() { return offset.clone(); }
        @Override public float[] rotation() { return rotation.clone(); }
        @Override public float[] scale() { return scale.clone(); }
        @Override public int[] textureOffset() { return textureOffset.clone(); }
    }

    public static TabulaModelContainer fromJson(JsonObject root) {
        Objects.requireNonNull(root, "root");
        String name = JsonUtils.getString(root, "modelName", "unnamed");
        int textureWidth = JsonUtils.getInt(root, "textureWidth", 64);
        int textureHeight = JsonUtils.getInt(root, "textureHeight", 32);
        JsonElement cubesElement = root.get("cubes");
        if (cubesElement == null || !cubesElement.isJsonArray()) {
            throw new IllegalArgumentException("Tabula model is missing its cubes array");
        }
        List<Cube> cubes = new ArrayList<>();
        for (JsonElement element : cubesElement.getAsJsonArray()) {
            cubes.add(parseCube(element.getAsJsonObject()));
        }
        return new TabulaModelContainer(name, textureWidth, textureHeight, cubes);
    }

    private static Cube parseCube(JsonObject object) {
        String name = JsonUtils.getString(object, "name", "unnamed");
        float[] dimensions = JsonUtils.getFloatArray(object, "dimensions", 3, new float[]{0, 0, 0});
        float[] position = JsonUtils.getFloatArray(object, "position", 3, new float[]{0, 0, 0});
        float[] offset = JsonUtils.getFloatArray(object, "offset", 3, new float[]{0, 0, 0});
        float[] rotation = JsonUtils.getFloatArray(object, "rotation", 3, new float[]{0, 0, 0});
        float[] scale = JsonUtils.getFloatArray(object, "scale", 3, new float[]{1, 1, 1});
        float[] uv = JsonUtils.getFloatArray(object, "txOffset", 2, new float[]{0, 0});
        int[] textureOffset = {(int) uv[0], (int) uv[1]};
        List<Cube> children = new ArrayList<>();
        JsonElement childElement = object.get("children");
        if (childElement != null && childElement.isJsonArray()) {
            JsonArray childArray = childElement.getAsJsonArray();
            for (JsonElement child : childArray) {
                children.add(parseCube(child.getAsJsonObject()));
            }
        }
        return new Cube(name, dimensions, position, offset, rotation, scale, textureOffset,
                JsonUtils.getBoolean(object, "txMirror", false),
                JsonUtils.getFloat(object, "mcScale", 0.0F),
                JsonUtils.getBoolean(object, "hidden", false), children);
    }
}
