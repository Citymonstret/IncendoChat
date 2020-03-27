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
package com.github.sauilitired.incendochat.chat;

import com.github.sauilitired.incendochat.event.ChannelSubscribeEvent;
import com.github.sauilitired.incendochat.event.ChannelUnsubscribeEvent;
import com.github.sauilitired.incendochat.players.ChatPlayer;
import com.github.sauilitired.incendochat.players.PlayerRegistry;
import com.github.sauilitired.incendochat.registry.Keyed;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;

public abstract class ChatChannel extends Keyed {

    private final UUID uuid = UUID.randomUUID();
    private final String key;
    private final ChannelConfiguration channelConfiguration;
    private final Collection<ChatPlayer> subscribers = new HashSet<>();
    public ChatChannel(@NotNull final String key,
        @NotNull final ChannelConfiguration channelConfiguration) {
        this.key = Preconditions.checkNotNull(key);
        this.channelConfiguration = Preconditions.checkNotNull(channelConfiguration);
        this.subscribers.add(PlayerRegistry.getRegistry().getServer());
        // Register the channel
        ChannelRegistry.getRegistry().register(this);
    }

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ChatChannel that = (ChatChannel) o;
        return new EqualsBuilder().append(uuid, that.uuid).append(key, that.key).isEquals();
    }

    @Override public int hashCode() {
        return new HashCodeBuilder(17, 37).append(uuid).append(key).toHashCode();
    }

    @NotNull @Override public final String getKey() {
        return this.key;
    }

    public abstract boolean isValid(@NotNull final ChatPlayer player);

    public void registerSubscriber(@NotNull final ChatPlayer player) {
        this.subscribers.add(Preconditions.checkNotNull(player));
        Bukkit.getPluginManager().callEvent(new ChannelSubscribeEvent(this, player));
    }

    public void deregisterSubscriber(@NotNull final ChatPlayer player) {
        this.subscribers.remove(Preconditions.checkNotNull(player));
        Bukkit.getPluginManager().callEvent(new ChannelUnsubscribeEvent(this, player));
    }

    @NotNull public Collection<ChatPlayer> getSubscribers() {
        return Collections.unmodifiableCollection(this.subscribers);
    }

    @NotNull public final ChannelConfiguration getChannelConfiguration() {
        return this.channelConfiguration;
    }

}
