package com.github.sauilitired.incendochat.chat;

import com.github.sauilitired.incendochat.event.ChannelCreationEvent;
import com.github.sauilitired.incendochat.registry.Registry;
import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class ChannelRegistry extends Registry<ChatChannel> {

    public static final ChannelRegistry registry = new ChannelRegistry();

    private GlobalChatChannel globalChatChannel;

    private ChannelRegistry() {
    }

    @NotNull public final GlobalChatChannel getGlobalChatChannel() {
        return this.globalChatChannel;
    }

    public void setGlobalChatChannel(@NotNull final GlobalChatChannel globalChatChannel) {
        this.globalChatChannel = Preconditions.checkNotNull(globalChatChannel);
    }

    @Override public void register(@NotNull final ChatChannel channel) {
        super.register(channel);
        // Call creation event
        Bukkit.getPluginManager().callEvent(new ChannelCreationEvent(channel));
    }

}
