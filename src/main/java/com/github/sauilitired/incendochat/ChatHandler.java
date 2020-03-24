package com.github.sauilitired.incendochat;

import com.github.sauilitired.incendochat.chat.ChannelRegistry;
import com.github.sauilitired.incendochat.chat.ChatChannel;
import com.github.sauilitired.incendochat.players.ChatPlayer;
import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class ChatHandler {

    public void handleMessage(@Nullable final ChatChannel forcedChannel,
        @NotNull final ChatPlayer player, @NotNull final String text) {
        Preconditions.checkNotNull(player);
        Preconditions.checkNotNull(text);
        final ChatChannel chatChannel;
        if (forcedChannel == null) {
            // Update all channels
            player.updateChannels();
            // Check if the player has a configured active channel
            final ChatChannel activeChannel = player.getActiveChannel();
            if (activeChannel != null && !activeChannel.isValid(player)) {
                chatChannel = activeChannel;
            } else {
                // If the player does not have an active channel, select the highest
                // priority active channel
                final List<ChatChannel> channels = new ArrayList<>(player.getActiveChannels());
                if (channels.isEmpty()) {
                    // Use the global channel
                    chatChannel = ChannelRegistry.registry.getGlobalChatChannel();
                } else {
                    channels.sort(Comparator.comparingInt(ChatChannel::getPriority).reversed());
                    chatChannel = channels.get(0);
                    // Update the active channel
                    player.setActiveChannel(chatChannel);
                }
            }
        } else {
            chatChannel = forcedChannel;
        }
        // Verify that the channel is valid
        if (!chatChannel.isValid(player)) {
            return;
        }
        final Collection<ChatPlayer> receivers = chatChannel.getSubscribers();
        for (final ChatPlayer receiver : receivers) {

        }
    }


}
