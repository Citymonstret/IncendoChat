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

import com.github.sauilitired.incendochat.event.ChannelCreationEvent;
import com.github.sauilitired.incendochat.registry.Registry;
import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

/**
 * {@link Registry} that holds all {@link ChatChannel channels}
 * {@inheritDoc}
 */
public class ChannelRegistry extends Registry<ChatChannel> {

    private static final ChannelRegistry registry = new ChannelRegistry();

    public static ChannelRegistry getRegistry() {
        return registry;
    }

    private GlobalChatChannel globalChatChannel;

    private ChannelRegistry() {
        super();
    }

    @NotNull public final GlobalChatChannel getGlobalChatChannel() {
        return this.globalChatChannel;
    }

    public void setGlobalChatChannel(@NotNull final GlobalChatChannel globalChatChannel) {
        this.globalChatChannel = Preconditions.checkNotNull(globalChatChannel);
    }

    @Override public void register(@NotNull final ChatChannel channel) {
        super.register(channel);
        // Call creation event
        Bukkit.getPluginManager().callEvent(new ChannelCreationEvent(channel));
    }

}
