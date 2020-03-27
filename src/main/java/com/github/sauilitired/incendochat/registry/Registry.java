/*
    Simple channel based chat plugin for Spigot
    Copyright (C) 2020 Alexander Söderberg

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
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
