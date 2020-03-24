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

    public final boolean equals(final Object other) {
        if (!(other instanceof Keyed)) {
            return false;
        }
        return ((Keyed) other).getKey().toLowerCase().equals(this.getKey().toLowerCase());
    }

    @Override public final int hashCode() {
        return this.getKey().hashCode();
    }

}
