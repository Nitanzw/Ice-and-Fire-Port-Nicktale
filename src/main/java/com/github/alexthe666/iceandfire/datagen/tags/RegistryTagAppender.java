package com.github.alexthe666.iceandfire.datagen.tags;

import java.util.function.Function;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagKey;

/** Adapts 26.2's key-based tag appender to registry values used by the legacy tag declarations. */
final class RegistryTagAppender<T> implements TagAppender<T> {
    private final TagAppender<T> delegate;
    private final Function<T, ResourceKey<T>> keyOf;

    private RegistryTagAppender(TagAppender<T> delegate, Function<T, ResourceKey<T>> keyOf) {
        this.delegate = delegate;
        this.keyOf = keyOf;
    }

    static <T> RegistryTagAppender<T> wrap(TagAppender<T> delegate, Function<T, ResourceKey<T>> keyOf) {
        return new RegistryTagAppender<>(delegate, keyOf);
    }

    public RegistryTagAppender<T> add(T element) {
        delegate.add(keyOf.apply(element));
        return this;
    }

    @SafeVarargs
    public final RegistryTagAppender<T> add(T... elements) {
        for (T element : elements) add(element);
        return this;
    }

    @Override
    public RegistryTagAppender<T> add(ResourceKey<T> element) {
        delegate.add(element);
        return this;
    }

    @Override
    public RegistryTagAppender<T> addOptional(ResourceKey<T> element) {
        delegate.addOptional(element);
        return this;
    }

    @Override
    public RegistryTagAppender<T> addTag(TagKey<T> tag) {
        delegate.addTag(tag);
        return this;
    }

    @Override
    public RegistryTagAppender<T> addOptionalTag(TagKey<T> tag) {
        delegate.addOptionalTag(tag);
        return this;
    }

    @Override
    public RegistryTagAppender<T> add(TagEntry entry) {
        delegate.add(entry);
        return this;
    }

    @Override
    public RegistryTagAppender<T> replace(boolean value) {
        delegate.replace(value);
        return this;
    }

    @Override
    public RegistryTagAppender<T> remove(ResourceKey<T> element) {
        delegate.remove(element);
        return this;
    }

    @Override
    public RegistryTagAppender<T> remove(TagKey<T> tag) {
        delegate.remove(tag);
        return this;
    }
}
