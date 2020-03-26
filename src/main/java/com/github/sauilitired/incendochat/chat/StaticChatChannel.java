package com.github.sauilitired.incendochat.chat;

import com.github.sauilitired.incendochat.players.ChatPlayer;
import org.jetbrains.annotations.NotNull;

public class StaticChatChannel extends ChatChannel {

    public StaticChatChannel(@NotNull final String key,
        @NotNull final ChannelConfiguration channelConfiguration) {
        super(key, channelConfiguration);
    }

    @Override public boolean isValid(@NotNull ChatPlayer player) {
        return true;
    }

}
