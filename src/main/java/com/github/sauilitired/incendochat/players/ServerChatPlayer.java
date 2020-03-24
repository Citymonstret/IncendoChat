package com.github.sauilitired.incendochat.players;

import com.github.sauilitired.incendochat.chat.ChatMessage;
import net.kyori.text.adapter.bukkit.TextAdapter;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

/**
 * {@link ChatPlayer} implementation backed by a
 * {@link org.bukkit.command.ConsoleCommandSender}
 */
public final class ServerChatPlayer extends ChatPlayer {

    @NotNull @Override public String getDisplayName() {
        return "Server";
    }

    @Override public void sendMessage(@NotNull final ChatMessage message) {
        TextAdapter.sendComponent(Bukkit.getConsoleSender(), message.getMessage());
    }

    @NotNull @Override public String getKey() {
        return "server";
    }

}
