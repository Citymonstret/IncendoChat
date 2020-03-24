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
