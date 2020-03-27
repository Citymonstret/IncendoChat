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
package com.github.sauilitired.incendochat.players;

import com.github.sauilitired.incendochat.chat.ChatMessage;
import com.google.common.base.Preconditions;
import net.kyori.text.Component;
import net.kyori.text.adapter.bukkit.TextAdapter;
import net.kyori.text.serializer.legacy.LegacyComponentSerializer;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * {@link ChatPlayer} implementation that is backed
 * by a {@link Player Bukkit player}
 */
public final class BukkitChatPlayer extends ChatPlayer {

    private final UUID uuid;
    private final Player bukkitPlayer;
    private final String key;

    public BukkitChatPlayer(@NotNull final Player bukkitPlayer) {
        this.bukkitPlayer = Preconditions.checkNotNull(bukkitPlayer);
        this.key = PlayerRegistry.getKey(bukkitPlayer);
        this.uuid = this.bukkitPlayer.getUniqueId();
    }

    /**
     * Get the backing {@link Player Bukkit player}
     *
     * @return Bukkit player that the player instance wraps
     */
    @NotNull public Player getBukkitPlayer() {
        return this.bukkitPlayer;
    }

    @Override public void sendMessage(@NotNull ChatMessage message) {
        TextAdapter.sendComponent(this.bukkitPlayer, message.getMessage());
    }

    @NotNull @Override public String getDisplayName() {
        return this.bukkitPlayer.getDisplayName();
    }

    @NotNull @Override public String getKey() {
        return this.key;
    }

    @Override public @NotNull String getName() {
        return this.bukkitPlayer.getName();
    }

    @Override public boolean hasPermission(@NotNull final String permission) {
        return this.bukkitPlayer.hasPermission(permission);
    }

    @Override public void sendMessage(@Nullable final String message) {
        if (message == null || message.isEmpty()) {
            return;
        }
        TextAdapter.sendComponent(this.bukkitPlayer, LegacyComponentSerializer.INSTANCE.deserialize(message, '&'));
    }

    @Override public void sendMessage(@Nullable final Component message) {
        if (message == null) {
            return;
        }
        TextAdapter.sendComponent(this.bukkitPlayer, message);
    }

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BukkitChatPlayer that = (BukkitChatPlayer) o;
        return new EqualsBuilder().append(uuid, that.uuid).append(key, that.key).isEquals();
    }

    @Override public int hashCode() {
        return new HashCodeBuilder(17, 37).append(uuid).append(key).toHashCode();
    }

}
