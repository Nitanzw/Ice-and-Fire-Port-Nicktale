// SPDX-License-Identifier: LGPL-3.0-or-later
package com.nicktale.api.client.model.container;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/** Small, strict JSON helpers for the public Tabula JSON shape. */
public final class JsonUtils {
    private JsonUtils() {
    }

    public static String getString(JsonObject object, String key, String fallback) {
        JsonElement value = object.get(key);
        return value == null || value.isJsonNull() ? fallback : value.getAsString();
    }

    public static int getInt(JsonObject object, String key, int fallback) {
        JsonElement value = object.get(key);
        return value == null || value.isJsonNull() ? fallback : value.getAsInt();
    }

    public static float getFloat(JsonObject object, String key, float fallback) {
        JsonElement value = object.get(key);
        return value == null || value.isJsonNull() ? fallback : value.getAsFloat();
    }

    public static boolean getBoolean(JsonObject object, String key, boolean fallback) {
        JsonElement value = object.get(key);
        return value == null || value.isJsonNull() ? fallback : value.getAsBoolean();
    }

    public static float[] getFloatArray(JsonObject object, String key, int length, float[] fallback) {
        JsonElement value = object.get(key);
        if (value == null || !value.isJsonArray()) {
            return fallback.clone();
        }
        JsonArray array = value.getAsJsonArray();
        if (array.size() != length) {
            throw new IllegalArgumentException("Expected " + length + " values in '" + key + "', got " + array.size());
        }
        float[] result = new float[length];
        for (int i = 0; i < length; i++) {
            result[i] = array.get(i).getAsFloat();
        }
        return result;
    }
}
