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

import com.github.sauilitired.incendochat.players.ChatPlayer;
import com.github.sauilitired.incendochat.players.PlayerRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public final class GlobalChatChannel extends ChatChannel {

    public GlobalChatChannel(final ChannelConfiguration channelConfiguration) {
        super("global", channelConfiguration);
    }

    @Override public boolean isValid(@NotNull ChatPlayer player) {
        return true;
    }

    @Override @NotNull public Collection<ChatPlayer> getSubscribers() {
        final Set<ChatPlayer> players = new HashSet<>(PlayerRegistry.getRegistry().getAll());
        players.add(PlayerRegistry.getRegistry().getServer());
        return players;
    }

}
