package com.github.sauilitired.incendochat;

import co.aikar.commands.PaperCommandManager;
import com.github.sauilitired.incendochat.chat.ChannelConfiguration;
import com.github.sauilitired.incendochat.chat.ChannelRegistry;
import com.github.sauilitired.incendochat.chat.GlobalChatChannel;
import com.github.sauilitired.incendochat.chat.StaticChatChannel;
import com.github.sauilitired.incendochat.commands.ChannelCommand;
import com.github.sauilitired.incendochat.listeners.ChatListener;
import com.github.sauilitired.incendochat.listeners.PlayerListener;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class IncendoChat extends JavaPlugin {

    private ChatHandler chatHandler;
    private ConfigurationSection messages;

    @Override public void onEnable() {
        this.chatHandler = new ChatHandler();

        // Save default configuration
        this.saveDefaultConfig();

        // Parse configuration
        final FileConfiguration fileConfiguration = this.getConfig();
        final ConfigurationSection channelSection = fileConfiguration.getConfigurationSection("channels");
        if (channelSection != null) {
            for (final String channelName : channelSection.getKeys(false)) {
                final ConfigurationSection channelConfiguration = channelSection.getConfigurationSection(channelName);
                if (channelConfiguration != null) {
                    final ChannelConfiguration channel = ChannelConfiguration.parse(channelConfiguration);
                    if (channelName.equalsIgnoreCase("global")) {
                        ChannelRegistry.registry.setGlobalChatChannel(new GlobalChatChannel(channel));
                    } else {
                        ChannelRegistry.registry.register(new StaticChatChannel(channelName.toLowerCase(), channel));
                    }
                }
            }
        }
        getLogger().info(String.format("Parsed %d chat channels", ChannelRegistry.registry.getAll().size()));

        // Read messages
        this.messages = fileConfiguration.getConfigurationSection("messages");

        // Register events
        getServer().getPluginManager().registerEvents(new ChatListener(this.chatHandler), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);

        // Register command
        final PaperCommandManager paperCommandManager = new PaperCommandManager(this);
        paperCommandManager.registerCommand(new ChannelCommand(this, paperCommandManager));
    }

    @NotNull public ConfigurationSection getMessages() {
        return this.messages;
    }

}
