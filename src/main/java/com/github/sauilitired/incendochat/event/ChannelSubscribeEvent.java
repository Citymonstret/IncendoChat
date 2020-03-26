package com.github.sauilitired.incendochat.event;

import com.github.sauilitired.incendochat.chat.ChatChannel;
import com.github.sauilitired.incendochat.players.ChatPlayer;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ChannelSubscribeEvent extends PlayerChannelEvent {

    private static final HandlerList handlers = new HandlerList();

    public ChannelSubscribeEvent(@NotNull final ChatChannel chatChannel, @NotNull final ChatPlayer player) {
        super(chatChannel, player);
    }

    @Override public @NotNull HandlerList getHandlers() {
        return handlers;
    }

}
