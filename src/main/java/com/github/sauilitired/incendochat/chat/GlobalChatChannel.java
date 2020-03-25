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
        final Set<ChatPlayer> players = new HashSet<>(PlayerRegistry.registry.getAll());
        players.add(PlayerRegistry.registry.getServer());
        return players;
    }

}
