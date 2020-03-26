package com.github.sauilitired.incendochat.event;

import com.github.sauilitired.incendochat.chat.ChatChannel;
import com.google.common.base.Preconditions;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;

public abstract class ChannelEvent extends Event {

    private final ChatChannel chatChannel;

    public ChannelEvent(@NotNull final ChatChannel chatChannel) {
        this.chatChannel = Preconditions.checkNotNull(chatChannel);
    }

    @NotNull public ChatChannel getChatChannel() {
        return this.chatChannel;
    }

}
