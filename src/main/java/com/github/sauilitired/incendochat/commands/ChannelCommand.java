package com.github.sauilitired.incendochat.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
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
import com.github.sauilitired.incendochat.chat.StaticChatChannel;
import com.github.sauilitired.incendochat.players.ChatPlayer;
import com.github.sauilitired.incendochat.players.PlayerRegistry;
import com.google.common.base.Preconditions;
import net.kyori.text.adapter.bukkit.TextAdapter;
import net.kyori.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@CommandAlias("channel|ch")
@CommandPermission("incendochat.channel")
public class ChannelCommand extends BaseCommand {

    private final IncendoChat incendoChat;

    public ChannelCommand(@NotNull final IncendoChat incendoChat, @NotNull final PaperCommandManager commandManager) {
        this.incendoChat = Preconditions.checkNotNull(incendoChat);
        Preconditions.checkNotNull(commandManager).getCommandCompletions().registerAsyncCompletion("channels", context -> {
            final ChatPlayer chatPlayer;
            if (context.getIssuer().isPlayer()) {
                chatPlayer = PlayerRegistry.registry.getPlayer(context.getIssuer().getIssuer());
            } else {
                chatPlayer = PlayerRegistry.registry.getServer();
            }
            final Collection<ChatChannel> channels = chatPlayer.getActiveChannels();
            final String selection = context.getConfig("selection", "not_in").toLowerCase();
            // We only tab complete static channels (for now)
            if (selection.equalsIgnoreCase("not_in")) {
                return ChannelRegistry.registry.getAll().stream().filter(channel -> channel instanceof StaticChatChannel)
                    .filter(channel -> !channels.contains(channel)).map(ChatChannel::getKey)
                    .collect(Collectors.toList());
            } else if (selection.equalsIgnoreCase("in")) {
                return channels.stream().filter(channel -> channel instanceof StaticChatChannel)
                    .map(ChatChannel::getKey).collect(Collectors.toList());
            } else {
                return Collections.emptyList();
            }
        });
    }

    @HelpCommand
    public void doHelp(final CommandSender sender, final CommandHelp help) {
        TextAdapter.sendComponent(sender, LegacyComponentSerializer.INSTANCE.deserialize(Preconditions
            .checkNotNull(incendoChat.getMessages().getString("help-header", "")), '&'));
        help.showHelp();
    }

    @Subcommand("l|list|channels")
    @CommandPermission("incendochat.channel.list")
    @Description("List all channels you are currently in, along with all available channels")
    public void doList(final CommandSender sender) {

    }

    @Subcommand("join")
    @CommandPermission("incendochat.channel.join")
    @CommandCompletion("@channels:selection=in")
    @Description("Join a channel")
    public void doJoin(final CommandSender sender, final String channel) {

    }

    @Subcommand("exit|quit|leave")
    @CommandPermission("incendochat.channel.leave")
    @CommandCompletion("@channels:selection=not_in")
    @Description("Leave a channel")
    public void doLeave(final CommandSender sender, final String channel) {

    }

}
