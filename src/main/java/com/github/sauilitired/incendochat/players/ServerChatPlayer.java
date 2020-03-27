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
import net.kyori.text.Component;
import net.kyori.text.serializer.plain.PlainComponentSerializer;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * {@link ChatPlayer} implementation backed by a
 * {@link org.bukkit.command.ConsoleCommandSender}
 */
public final class ServerChatPlayer extends ChatPlayer {

    private final UUID uuid = UUID.randomUUID();

    @NotNull @Override public String getDisplayName() {
        return "Server";
    }

    @Override public void sendMessage(@NotNull final ChatMessage message) {
        this.sendMessage(message.getMessage());
    }

    @NotNull @Override public String getKey() {
        return "server";
    }

    @Override public @NotNull String getName() {
        return "Server";
    }

    @Override public boolean hasPermission(@NotNull final String permission) {
        return true;
    }

    @Override public void sendMessage(@Nullable final String message) {
        if (message == null || message.isEmpty()) {
            return;
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    @Override public void sendMessage(@Nullable final Component message) {
        if (message == null) {
            return;
        }
        Bukkit.getConsoleSender().sendMessage(PlainComponentSerializer.INSTANCE.serialize(message));
    }

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ServerChatPlayer that = (ServerChatPlayer) o;
        return new EqualsBuilder().append(uuid, that.uuid).isEquals();
    }

    @Override public int hashCode() {
        return new HashCodeBuilder(17, 37).append(uuid).toHashCode();
    }

}
