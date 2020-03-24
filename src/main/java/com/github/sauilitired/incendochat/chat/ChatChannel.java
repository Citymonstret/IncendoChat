package com.github.sauilitired.incendochat.chat;

import com.github.sauilitired.incendochat.players.ChatPlayer;
import com.github.sauilitired.incendochat.players.PlayerRegistry;
import com.github.sauilitired.incendochat.registry.Keyed;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public abstract class ChatChannel extends Keyed {

    private final String key;
    private final int priority;
    private final Collection<ChatPlayer> subscribers = new HashSet<>();

    public ChatChannel(@NotNull final String key, @NotNull final int priority) {
        this.key = Preconditions.checkNotNull(key);
        this.priority = priority;
        this.subscribers.add(PlayerRegistry.registry.getServer());
        // Register the channel
        ChannelRegistry.registry.register(this);
    }

    @NotNull @Override public final String getKey() {
        return this.key;
    }

    public abstract boolean isValid(@NotNull final ChatPlayer player);

    public void registerSubscriber(@NotNull final ChatPlayer player) {
        this.subscribers.add(Preconditions.checkNotNull(player));
    }

    public void deregisterSubscriber(@NotNull final ChatPlayer player) {
        this.subscribers.remove(Preconditions.checkNotNull(player));
    }

    @NotNull public Collection<ChatPlayer> getSubscribers() {
        return Collections.unmodifiableCollection(this.subscribers);
    }

    public final int getPriority() {
        return this.priority;
    }

}
