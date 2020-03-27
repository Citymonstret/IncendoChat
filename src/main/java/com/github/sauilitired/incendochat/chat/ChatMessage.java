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
import com.google.common.base.Preconditions;
import net.kyori.text.Component;
import org.jetbrains.annotations.NotNull;

/**
 * This object represents a chat message, sent in a {@link ChatChannel channel}
 * by a {@link ChatPlayer player}
 */
public final class ChatMessage {

    private final ChatChannel channel;
    private final ChatPlayer sender;
    private final Component message;

    public ChatMessage(@NotNull final ChatChannel chatChannel, @NotNull final ChatPlayer sender,
        @NotNull final Component message) {
        this.channel = Preconditions.checkNotNull(chatChannel);
        this.sender = Preconditions.checkNotNull(sender);
        this.message = Preconditions.checkNotNull(message);
    }

    /**
     * Get the message that was sent
     *
     * @return Sent message
     */
    @NotNull public Component getMessage() {
        return this.message;
    }

    /**
     * Get the sender of the message
     *
     * @return Message sender
     */
    @NotNull public ChatPlayer getSender() {
        return this.sender;
    }

    /**
     * Get the channel the message was sent in
     *
     * @return Chat channel the message was sent in
     */
    @NotNull public ChatChannel getChannel() {
        return this.channel;
    }

}
