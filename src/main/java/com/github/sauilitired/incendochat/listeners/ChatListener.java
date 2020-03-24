package com.github.sauilitired.incendochat.listeners;

import com.github.sauilitired.incendochat.ChatHandler;
import com.github.sauilitired.incendochat.players.ChatPlayer;
import com.github.sauilitired.incendochat.players.PlayerRegistry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;

public final class ChatListener implements Listener {

    private final ChatHandler chatHandler;

    public ChatListener(@NotNull final ChatHandler chatHandler) {
        this.chatHandler = chatHandler;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onChatEvent(final AsyncPlayerChatEvent event) {
        // If we get this far, and no other plugin has intercepted the message, we just
        // cancel it and deal with it ourselves
        event.setCancelled(true);
        // Now we retrieve player info, and handle it internally
        final ChatPlayer chatPlayer = PlayerRegistry.registry.getPlayer(event.getPlayer());
        this.chatHandler.handleMessage(chatPlayer, event.getMessage());
    }

}
