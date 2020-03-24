package com.github.sauilitired.incendochat.listeners;

import com.github.sauilitired.incendochat.players.ChatPlayer;
import com.github.sauilitired.incendochat.players.PlayerRegistry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        final ChatPlayer player = PlayerRegistry.registry.getPlayer(event.getPlayer());
        PlayerRegistry.registry.deregister(player);
    }

}
