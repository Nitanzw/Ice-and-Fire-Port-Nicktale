// SPDX-License-Identifier: LGPL-3.0-or-later
package com.nicktale.api.client.model;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nicktale.api.client.model.container.TabulaModelContainer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/** Reads the JSON model entry embedded in a Tabula archive. */
public final class TabulaModelHandler {
    public static final TabulaModelHandler INSTANCE = new TabulaModelHandler();

    private TabulaModelHandler() {
    }

    public TabulaModelContainer loadTabulaModel(InputStream jsonStream) throws IOException {
        Objects.requireNonNull(jsonStream, "jsonStream");
        try (InputStream input = jsonStream; InputStreamReader reader = new InputStreamReader(input, StandardCharsets.UTF_8)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
            return TabulaModelContainer.fromJson(root);
        } catch (RuntimeException exception) {
            throw new IOException("Invalid Tabula model JSON", exception);
        }
    }
}
