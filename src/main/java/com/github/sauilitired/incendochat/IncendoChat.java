package com.github.sauilitired.incendochat;

import com.github.sauilitired.incendochat.listeners.ChatListener;
import com.github.sauilitired.incendochat.listeners.PlayerListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class IncendoChat extends JavaPlugin {

    private ChatHandler chatHandler;

    @Override public void onEnable() {
        this.chatHandler = new ChatHandler();
        getServer().getPluginManager().registerEvents(new ChatListener(this.chatHandler), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
    }

}
