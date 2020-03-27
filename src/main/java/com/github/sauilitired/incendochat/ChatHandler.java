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
package com.github.sauilitired.incendochat;

import com.github.sauilitired.incendochat.chat.ChannelConfiguration;
import com.github.sauilitired.incendochat.chat.ChannelRegistry;
import com.github.sauilitired.incendochat.chat.ChatChannel;
import com.github.sauilitired.incendochat.chat.ChatMessage;
import com.github.sauilitired.incendochat.event.ChannelMessageEvent;
import com.github.sauilitired.incendochat.persistence.PersistenceHandler;
import com.github.sauilitired.incendochat.players.BukkitChatPlayer;
import com.github.sauilitired.incendochat.players.ChatPlayer;
import com.google.common.base.Preconditions;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.text.TextComponent;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.serializer.legacy.LegacyComponentSerializer;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

public class ChatHandler {

    private static final Pattern
        STRIP_COLOR_PATTERN = Pattern.compile("(?i)&[0-9A-FK-OR]");

    private static String stripColor(@NotNull final String input) {
        return STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
    }

    private final PersistenceHandler persistenceHandler;

    public ChatHandler(@NotNull final PersistenceHandler persistenceHandler) {
        this.persistenceHandler = Preconditions.checkNotNull(persistenceHandler);
    }

    public void handleMessage(@Nullable final ChatChannel forcedChannel,
        @NotNull final ChatPlayer player, @NotNull final String text) {
        Preconditions.checkNotNull(player);
        Preconditions.checkNotNull(text);
        Runnable messageTask = () -> {
            final ChatChannel chatChannel;
            if (forcedChannel == null) {
                // Update all channels
                player.updateChannels();
                // Check if the player has a configured active channel
                final ChatChannel activeChannel = player.getActiveChannel();
                if (activeChannel != null && activeChannel.isValid(player)) {
                    chatChannel = activeChannel;
                } else {
                    // If the player does not have an active channel, select the highest
                    // priority active channel
                    final List<ChatChannel> channels = new ArrayList<>(player.getActiveChannels());
                    if (channels.isEmpty()) {
                        // Use the global channel
                        chatChannel = ChannelRegistry.getRegistry().getGlobalChatChannel();
                    } else {
                        channels.sort(Comparator.<ChatChannel>comparingInt(
                            channel -> channel.getChannelConfiguration().getPriority()).reversed());
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

            // Get the recipients
            Collection<ChatPlayer> recipients = chatChannel.getSubscribers();

            // Non-final copy of the text, so that it can be replaced in the event
            String message = text;

            // Send the event and check for cancellation
            final ChannelMessageEvent channelMessageEvent = new ChannelMessageEvent(chatChannel, player, message, recipients);
            Bukkit.getPluginManager().callEvent(channelMessageEvent);
            if (channelMessageEvent.isCancelled()) {
                return;
            } else {
                recipients = channelMessageEvent.getRecipients();
                message = channelMessageEvent.getMessage();
            }

            for (final ChatPlayer receiver : recipients) {
                // Go through all message parts and compile them
                final TextComponent.Builder builder = TextComponent.builder();
                for (final ChannelConfiguration.ChannelFormatSection channelFormatSection :
                    chatChannel.getChannelConfiguration().getChannelFormatSections()) {
                    if (!channelFormatSection.getPermission().isEmpty() &&
                        !player.hasPermission(channelFormatSection.getPermission())) {
                        continue;
                    }
                    final TextComponent.Builder innerBuilder = TextComponent.builder();
                    final String textFormat = this.handleText(chatChannel, player, channelFormatSection.getText());
                    String messageText = stripColor(message);
                    if (chatChannel.getChannelConfiguration().getPingFormat() != null && message.contains(String.format("@%s", receiver.getName()))) {
                        messageText = StringUtils.replaceIgnoreCase(messageText, "@" + receiver.getName(),
                            ChatColor.translateAlternateColorCodes('&',
                                chatChannel.getChannelConfiguration().getPingFormat().replace("%name%", receiver.getName())));
                    }
                    innerBuilder.append(LegacyComponentSerializer.INSTANCE.deserialize(textFormat.replace("%message%", messageText), '&'));
                    if (channelFormatSection.getHoverText() != null && !channelFormatSection.getHoverText().isEmpty()) {
                        innerBuilder.hoverEvent(HoverEvent.of(HoverEvent.Action.SHOW_TEXT, LegacyComponentSerializer
                            .INSTANCE.deserialize(handleText(chatChannel, player, channelFormatSection.getHoverText()), '&')));
                    } else {
                        innerBuilder.hoverEvent(null);
                    }
                    if (channelFormatSection.getClickText() != null && channelFormatSection.getClickAction() != null &&
                        !channelFormatSection.getClickText().isEmpty()) {
                        innerBuilder.clickEvent(ClickEvent.of(channelFormatSection.getClickAction(),
                            handleText(chatChannel, player, channelFormatSection.getClickText())));
                    } else {
                        innerBuilder.clickEvent(null);
                    }
                    builder.append(innerBuilder.build());
                }
                receiver.sendMessage(new ChatMessage(chatChannel, player, builder.build(), message));
            }
            // Log message
            this.persistenceHandler.logMessage(new ChatMessage(chatChannel, player, null,
                ChatColor.stripColor(message)));
        };
        if (Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().runTaskAsynchronously(IncendoChat
                .getPlugin(IncendoChat.class), messageTask);
        } else {
            messageTask.run();
        }
    }

    @NotNull private String handleText(@NotNull final ChatChannel channel,
        @NotNull final ChatPlayer sender, @NotNull final String format) {
        Preconditions.checkNotNull(channel);
        Preconditions.checkNotNull(sender);
        Preconditions.checkNotNull(format);
        final Player player;
        if (sender instanceof BukkitChatPlayer) {
            player = ((BukkitChatPlayer) sender).getBukkitPlayer();
        } else {
            player = null;
        }
        return PlaceholderAPI.setPlaceholders(player, format).replace("%channel%",
            channel.getChannelConfiguration().getDisplayName()).replace("%channel_id%",
            channel.getKey().toLowerCase());
    }

}
