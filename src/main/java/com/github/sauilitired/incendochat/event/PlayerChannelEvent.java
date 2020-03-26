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
