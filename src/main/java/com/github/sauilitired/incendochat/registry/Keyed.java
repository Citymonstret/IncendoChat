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

import org.jetbrains.annotations.NotNull;

/**
 * Represents an object that has a (constant) key attached
 * to it
 */
public abstract class Keyed {

    /**
     * Get the constant key associated with the object
     *
     * @return Constant non-null key value
     */
    @NotNull public abstract String getKey();

    @Override public String toString() {
        return String.format("Keyed{class=%s,key=%s}", this.getClass().getSimpleName(), this.getKey());
    }

}
