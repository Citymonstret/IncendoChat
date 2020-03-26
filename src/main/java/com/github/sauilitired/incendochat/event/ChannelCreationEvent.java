package com.github.sauilitired.incendochat.event;

import com.github.sauilitired.incendochat.chat.ChatChannel;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ChannelCreationEvent extends ChannelEvent {

    private static final HandlerList handlers = new HandlerList();

    public ChannelCreationEvent(@NotNull final ChatChannel chatChannel) {
        super(chatChannel);
    }

    @NotNull @Override public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
