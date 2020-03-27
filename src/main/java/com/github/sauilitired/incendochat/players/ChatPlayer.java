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
package com.github.sauilitired.incendochat.players;

import com.github.sauilitired.incendochat.IncendoChat;
import com.github.sauilitired.incendochat.chat.ChannelRegistry;
import com.github.sauilitired.incendochat.chat.ChatChannel;
import com.github.sauilitired.incendochat.chat.ChatMessage;
import com.github.sauilitired.incendochat.registry.Keyed;
import com.google.common.base.Preconditions;
import net.kyori.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A player that is able to chat
 */
public abstract class ChatPlayer extends Keyed {

    private final Collection<ChatChannel> activeChannels = new HashSet<>();
    private ChatChannel activeChannel;

    /**
     * Get the player display name. This is the name
     * that shows up in chat messages, etc
     *
     * @return Player display name
     */
    @NotNull public abstract String getDisplayName();

    /**
     * Get the player name
     *
     * @return Player name
     */
    @NotNull public abstract String getName();

    /**
     * Send a message to the player
     *
     * @param message Chat message
     */
    public abstract void sendMessage(@NotNull final ChatMessage message);

    /**
     * Send a message to the player
     *
     * @param message Message
     */
    public abstract void sendMessage(@Nullable final String message);

    /**
     * Send a message to the player
     *
     * @param message Message
     */
    public abstract void sendMessage(@Nullable final Component message);

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
        if (this.activeChannel != null && this.activeChannel.equals(activeChannel)) {
            return;
        }
        this.activeChannel = activeChannel;
        if (activeChannel != null) {
            this.sendMessage(Objects.requireNonNull(IncendoChat.getPlugin(IncendoChat.class).getMessages()
                .getString("active-channel-set", "")).replace("%channel%", activeChannel.getKey()));
        }
    }

    /**
     * Register a new active channel for the player
     *
     * @param chatChannel Channel to join
     */
    public void joinChannel(@NotNull final ChatChannel chatChannel) {
        if (this.activeChannels.contains(Preconditions.checkNotNull(chatChannel))) {
            return;
        }
        chatChannel.registerSubscriber(this);
        this.activeChannels.add(chatChannel);
        this.sendMessage(Objects.requireNonNull(
            IncendoChat.getPlugin(IncendoChat.class).getMessages().getString("channel-joined", ""))
            .replace("%channel%", chatChannel.getKey()));
        if (this.activeChannel == null) {
            this.setActiveChannel(chatChannel);
        }
    }

    /**
     * Remove an active channel for the player
     *
     * @param chatChannel Channel to leave
     */
    public void leaveChannel(@NotNull final ChatChannel chatChannel) {
        if (!this.activeChannels.contains(Preconditions.checkNotNull(chatChannel))) {
            return;
        }
        chatChannel.deregisterSubscriber(this);
        this.activeChannels.remove(chatChannel);
        this.sendMessage(Objects.requireNonNull(
            IncendoChat.getPlugin(IncendoChat.class).getMessages().getString("channel-left", ""))
            .replace("%channel%", chatChannel.getKey()));
    }

    /**
     * Update the active channels for the player. Will remove all channels
     * that are no longer valid.
     */
    public void updateChannels() {
        final Set<ChatChannel> toLeave = new HashSet<>();
        for (final ChatChannel chatChannel : this.activeChannels) {
            if (chatChannel.isValid(this)) {
                continue;
            }
            toLeave.add(chatChannel);
        }
        for (final ChatChannel chatChannel : toLeave) {
            this.leaveChannel(chatChannel);
        }
        if (!this.activeChannels.contains(this.activeChannel)) {
            this.setActiveChannel(null);
        }
        if (!this.activeChannels.contains(ChannelRegistry.getRegistry().getGlobalChatChannel())) {
            this.joinChannel(ChannelRegistry.getRegistry().getGlobalChatChannel());
        }
    }

    /**
     * Check if the player has a specific permission node
     *
     * @param permission Permission node
     * @return True if the player has the permission, false if not
     */
    public abstract boolean hasPermission(@NotNull final String permission);

}
