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
package com.github.sauilitired.incendochat.event;

import com.github.sauilitired.incendochat.chat.ChatChannel;
import com.github.sauilitired.incendochat.players.ChatPlayer;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Event sent when a {@link ChatPlayer} subscribes to a {@link ChatChannel}
 */
public class ChannelSubscribeEvent extends PlayerChannelEvent {

    private static final HandlerList handlers = new HandlerList();

    public ChannelSubscribeEvent(@NotNull final ChatChannel chatChannel, @NotNull final ChatPlayer player) {
        super(chatChannel, player);
    }

    @Override public @NotNull HandlerList getHandlers() {
        return handlers;
    }

}
