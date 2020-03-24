package com.github.sauilitired.incendochat.players;

import com.github.sauilitired.incendochat.chat.ChatChannel;
import com.github.sauilitired.incendochat.chat.ChatMessage;
import com.github.sauilitired.incendochat.registry.Keyed;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

/**
 * A player that is able to chat
 */
public abstract class ChatPlayer extends Keyed {

    private final Collection<ChatChannel> activeChannels = new HashSet<>();
    private ChatChannel activeChannel;

    /**
     * Get the player display name. This is the name
     * that shows up in chat messages, etc.
     *
     * @return Player display name
     */
    @NotNull public abstract String getDisplayName();

    /**
     * Send a message to the player
     *
     * @param message Chat message
     */
    public abstract void sendMessage(@NotNull final ChatMessage message);

    /**
     * Get all channels the player is currently active in
     *
     * @return Unmodifiable view of the channel collection
     */
    @NotNull public final Collection<ChatChannel> getActiveChannels() {
        this.updateChannels();
        return Collections.unmodifiableCollection(this.activeChannels);
    }

    /**
     * Get the channel the player is currently active in. May be null
     *
     * @return Currently active channel
     */
    @Nullable public ChatChannel getActiveChannel() {
        return this.activeChannel;
    }

    /**
     * Update the active channel for the player
     *
     * @param activeChannel New channel, may be null
     */
    public void setActiveChannel(@Nullable final ChatChannel activeChannel) {
        this.activeChannel = activeChannel;
    }

    /**
     * Register a new active channel for the player
     *
     * @param chatChannel Channel to join
     */
    public void joinChannel(@NotNull final ChatChannel chatChannel) {
        this.activeChannels.add(Preconditions.checkNotNull(chatChannel));
    }

    /**
     * Update the active channels for the player. Will remove all channels
     * that are no longer valid.
     */
    public void updateChannels() {
        final Collection<ChatChannel> chatChannels = new ArrayList<>(this.activeChannels);
        for (final ChatChannel chatChannel : chatChannels) {
            if (!chatChannel.isValid(this)) {
                this.activeChannels.remove(chatChannel);
            }
        }
        if (!this.activeChannels.contains(this.activeChannel)) {
            this.setActiveChannel(null);
        }
    }

}
