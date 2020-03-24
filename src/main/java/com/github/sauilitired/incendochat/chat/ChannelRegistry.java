package com.github.sauilitired.incendochat.chat;

import com.github.sauilitired.incendochat.registry.Registry;
import org.jetbrains.annotations.NotNull;

public class ChannelRegistry extends Registry<ChatChannel> {

    public static final ChannelRegistry registry = new ChannelRegistry();

    private final GlobalChatChannel globalChatChannel;

    private ChannelRegistry() {
        this.globalChatChannel = new GlobalChatChannel();
    }

    @NotNull public final GlobalChatChannel getGlobalChatChannel() {
        return this.globalChatChannel;
    }

}
