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
package com.github.sauilitired.incendochat.chat.fragments;

import com.github.sauilitired.incendochat.players.ChatPlayer;
import net.kyori.text.TextComponent;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * A fragment is a part of a chat message that gets formatted externally
 * (from the perspective of the channel)
 *
 * Examples of this would be items ([i]), locations, etc.
 */
public abstract class ChatFragment {

    private final Collection<String> formatKeys;

    protected ChatFragment(final String ... keys) {
        this.formatKeys = Arrays.asList(keys);
    }

    /**
     * Create the fragment
     *
     * @param player Player sending the message
     * @return Non-null component. If the text is to be replaced by nothing,
     *         return an empty text component
     */
    @NotNull public abstract TextComponent createFragment(@NotNull final ChatPlayer player);

    /**
     * Get all the keys that are handled by this fragment. The syntax
     * in the actual message is [KEY]
     *
     * @return Unmodifiable view of the keys
     */
    @NotNull public final Collection<String> getFormatKeys() {
        return Collections.unmodifiableCollection(this.formatKeys);
    }

    @Override public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ChatFragment that = (ChatFragment) o;
        return new EqualsBuilder().append(formatKeys, that.formatKeys).isEquals();
    }

    @Override public int hashCode() {
        return new HashCodeBuilder(17, 37).append(formatKeys).toHashCode();
    }

}
