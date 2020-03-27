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
package com.github.sauilitired.incendochat.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.InvalidCommandArgument;
import co.aikar.commands.PaperCommandManager;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Description;
import co.aikar.commands.annotation.HelpCommand;
import co.aikar.commands.annotation.Subcommand;
import com.github.sauilitired.incendochat.IncendoChat;
import com.github.sauilitired.incendochat.chat.ChannelRegistry;
import com.github.sauilitired.incendochat.chat.ChatChannel;
import com.github.sauilitired.incendochat.chat.GlobalChatChannel;
import com.github.sauilitired.incendochat.chat.StaticChatChannel;
import com.github.sauilitired.incendochat.players.ChatPlayer;
import com.github.sauilitired.incendochat.players.PlayerRegistry;
import com.google.common.base.Preconditions;
import net.kyori.text.TextComponent;
import net.kyori.text.adapter.bukkit.TextAdapter;
import net.kyori.text.event.ClickEvent;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@CommandAlias("channel|ch")
@CommandPermission("incendochat.channel")
@SuppressWarnings("unused")
public final class ChannelCommand extends BaseCommand {

    private final IncendoChat incendoChat;

    public ChannelCommand(@NotNull final IncendoChat incendoChat, @NotNull final PaperCommandManager commandManager) {
        this.incendoChat = Preconditions.checkNotNull(incendoChat);
        Preconditions.checkNotNull(commandManager).getCommandCompletions().registerAsyncCompletion("channels", context -> {
            final ChatPlayer chatPlayer = PlayerRegistry.getRegistry().getPlayer(context.getSender());
            final Collection<ChatChannel> channels = chatPlayer.getActiveChannels();
            final String selection = context.getConfig("selection", "not_in").toLowerCase();
            // We only tab complete static channels (for now)
            if (selection.equalsIgnoreCase("not_in")) {
                return ChannelRegistry.getRegistry().getAll().stream().filter(channel -> channel instanceof StaticChatChannel)
                    .filter(channel -> !channels.contains(channel)).map(ChatChannel::getKey)
                    .collect(Collectors.toList());
            } else if (selection.equalsIgnoreCase("in")) {
                return channels.stream().filter(channel -> channel instanceof StaticChatChannel)
                    .map(ChatChannel::getKey).collect(Collectors.toList());
            } else {
                return Collections.emptyList();
            }
        });
        commandManager.getCommandContexts().registerContext(ChatChannel.class, context -> {
            final String id = context.popFirstArg().toLowerCase();
            final ChatChannel chatChannel = ChannelRegistry.getRegistry().get(id).orElse(null);
            if (chatChannel == null) {
                throw new InvalidCommandArgument("There is no channel with that ID: " + id);
            }
            if (!(chatChannel instanceof StaticChatChannel)) {
                throw new InvalidCommandArgument("You cannot perform this action on that channel");
            }
            return chatChannel;
        });
        commandManager.getCommandContexts().registerIssuerAwareContext(ChatPlayer.class, context ->
            PlayerRegistry.getRegistry().getPlayer(context.getSender()));
        // Enable help
        //noinspection deprecation
        commandManager.enableUnstableAPI("help");
    }

    @HelpCommand
    public void doHelp(final CommandSender sender, final CommandHelp help) {
        TextAdapter.sendComponent(sender, LegacyComponentSerializer.INSTANCE.deserialize(Preconditions
            .checkNotNull(incendoChat.getMessages().getString("help-header", "")), '&'));
        help.showHelp();
    }

    @Subcommand("list|channels|l")
    @CommandPermission("incendochat.channel.list")
    @Description("List all channels you are currently in, along with all available channels")
    public void doList(final ChatPlayer player) {
        player.sendMessage(getMessage("list-header"));
        player.sendMessage(getMessage("list-header-active"));
        for (final ChatChannel chatChannel : player.getActiveChannels()) {
            if (!(chatChannel instanceof StaticChatChannel)) {
                continue;
            }
            final String text;
            if (chatChannel.equals(player.getActiveChannel())) {
                text = getMessage("list-item-current");
            } else {
                text = getMessage("list-item");
            }
            player.sendMessage(TextComponent.builder().append(LegacyComponentSerializer
                .INSTANCE.deserialize(text.replace("%channel%",
                    chatChannel.getChannelConfiguration().getDisplayName()), '&')).hoverEvent(HoverEvent.of(
                HoverEvent.Action.SHOW_TEXT, LegacyComponentSerializer.INSTANCE.deserialize(getMessage("list-hover-leave"),
                    '&'))).clickEvent(ClickEvent.of(ClickEvent.Action.RUN_COMMAND, "/channel leave " + chatChannel.getKey()))
                .build());
        }
        player.sendMessage(getMessage("list-header-available"));
        for (final ChatChannel chatChannel : ChannelRegistry.getRegistry().getAll()) {
            if (player.getActiveChannels().contains(chatChannel) || (!chatChannel.getChannelConfiguration().getPermission().isEmpty() &&
                !player.hasPermission(chatChannel.getChannelConfiguration().getPermission())) ||
                !(chatChannel instanceof StaticChatChannel)) {
                continue;
            }
            player.sendMessage(TextComponent.builder().append(LegacyComponentSerializer
                .INSTANCE.deserialize(getMessage("list-item").replace("%channel%",
                    chatChannel.getChannelConfiguration().getDisplayName()), '&')).hoverEvent(HoverEvent.of(
                HoverEvent.Action.SHOW_TEXT, LegacyComponentSerializer.INSTANCE.deserialize(getMessage("list-hover-join"),
                    '&'))).clickEvent(ClickEvent.of(ClickEvent.Action.RUN_COMMAND, "/channel join " + chatChannel.getKey()))
                .build());
        }
    }

    @Subcommand("join")
    @CommandPermission("incendochat.channel.join")
    @CommandCompletion("@channels:selection=not_in")
    @Description("Join a channel")
    public void doJoin(final ChatPlayer player, final ChatChannel channel) {
        if (!channel.getChannelConfiguration().getPermission().isEmpty() &&
            !player.hasPermission(channel.getChannelConfiguration().getPermission())) {
            throw new InvalidCommandArgument("You are not allowed to join that channel");
        }
        if (player.getActiveChannels().contains(channel)) {
            throw new InvalidCommandArgument("You are already in that channel");
        }
        player.joinChannel(channel);
    }

    @Subcommand("exit|quit|leave")
    @CommandPermission("incendochat.channel.leave")
    @CommandCompletion("@channels:selection=in")
    @Description("Leave a channel")
    public void doLeave(final ChatPlayer player, final ChatChannel channel) {
        if (!player.getActiveChannels().contains(channel)) {
            throw new InvalidCommandArgument("You are not in that channel");
        }
        if (channel instanceof GlobalChatChannel) {
            throw new InvalidCommandArgument("You cannot leave the global channel");
        }
        player.leaveChannel(channel);
    }

    @Subcommand("set|activate")
    @CommandPermission("incendochat.channel.set")
    @CommandCompletion("@channels:selection=in")
    @Description("Mark the channel as your current channel")
    public void doSet(final ChatPlayer player, final ChatChannel channel) {
        if (!player.getActiveChannels().contains(channel)) {
            throw new InvalidCommandArgument("You are not in that channel");
        }
        player.setActiveChannel(channel);
    }

    @Subcommand("send|chat|use|s")
    @CommandPermission("incendochat.channel.send")
    @CommandCompletion("@channels:selection=in")
    @Description("Send a message to a specific channel")
    public void doSend(final ChatPlayer player, final ChatChannel channel, final String message) {
        if (!player.getActiveChannels().contains(channel)) {
            throw new InvalidCommandArgument("You are not in that channel");
        }
        this.incendoChat.getChatHandler().handleMessage(channel, player, message);
    }

    @NotNull final String getMessage(final String key) {
        return Optional.ofNullable(this.incendoChat.getMessages().getString(key, ""))
            .orElse("");
    }

}
