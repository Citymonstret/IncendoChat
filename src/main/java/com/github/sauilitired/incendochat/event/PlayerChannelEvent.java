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
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

public abstract class PlayerChannelEvent extends ChannelEvent {

    private final ChatPlayer chatPlayer;

    public PlayerChannelEvent(@NotNull final ChatChannel chatChannel,
        @NotNull final ChatPlayer chatPlayer) {
        super(chatChannel);
        this.chatPlayer = Preconditions.checkNotNull(chatPlayer);
    }

    @NotNull public ChatPlayer getChatPlayer() {
        return this.chatPlayer;
    }

}
