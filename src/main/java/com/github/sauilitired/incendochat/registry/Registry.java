package com.github.sauilitired.incendochat.registry;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Registry<T extends Keyed> {

    private final Map<String, T> registry;

    public Registry() {
        this.registry = new ConcurrentHashMap<>();
    }

    @NotNull public final Optional<T> get(@NotNull final String key) {
        return Optional.ofNullable(this.registry.get(Preconditions.checkNotNull(key).toLowerCase()));
    }

    public void register(@NotNull final T object) {
        Preconditions.checkNotNull(object);
        this.registry.put(object.getKey().toLowerCase(), object);
    }

    public void deregister(@NotNull final T object) {
        Preconditions.checkNotNull(object);
        this.registry.remove(object.getKey().toLowerCase());
    }

    @NotNull public final Collection<T> getAll() {
        return Collections.unmodifiableCollection(this.registry.values());
    }

}
