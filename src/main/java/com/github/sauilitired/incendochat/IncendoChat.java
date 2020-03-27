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

import co.aikar.commands.PaperCommandManager;
import com.github.sauilitired.incendochat.chat.ChannelConfiguration;
import com.github.sauilitired.incendochat.chat.ChannelRegistry;
import com.github.sauilitired.incendochat.chat.GlobalChatChannel;
import com.github.sauilitired.incendochat.chat.StaticChatChannel;
import com.github.sauilitired.incendochat.commands.ChannelCommand;
import com.github.sauilitired.incendochat.listeners.ChatListener;
import com.github.sauilitired.incendochat.listeners.PlayerListener;
import com.github.sauilitired.incendochat.persistence.PersistenceHandler;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class IncendoChat extends JavaPlugin {

    private ChatHandler chatHandler;
    private ConfigurationSection messages;
    private PersistenceHandler persistenceHandler;

    @Override public void onEnable() {

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
                        ChannelRegistry.getRegistry().setGlobalChatChannel(new GlobalChatChannel(channel));
                    } else {
                        ChannelRegistry.getRegistry().register(new StaticChatChannel(channelName.toLowerCase(), channel));
                    }
                }
            }
        }
        getLogger().info(String.format("Parsed %d chat channels", ChannelRegistry.getRegistry().getAll().size()));

        // Read messages
        this.messages = fileConfiguration.getConfigurationSection("messages");

        // Setup persistence handler
        this.persistenceHandler = new PersistenceHandler(fileConfiguration.getConfigurationSection("persistence"));

        // Setup chat handler
        this.chatHandler = new ChatHandler(this.persistenceHandler);

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

    @NotNull public ChatHandler getChatHandler() {
        return this.chatHandler;
    }

    @NotNull public PersistenceHandler getPersistenceHandler() {
        return this.persistenceHandler;
    }

}
