package com.github.sauilitired.incendochat.players;

import com.github.sauilitired.incendochat.chat.ChatMessage;
import com.google.common.base.Preconditions;
import net.kyori.text.adapter.bukkit.TextAdapter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * {@link ChatPlayer} implementation that is backed
 * by a {@link Player Bukkit player}
 */
public final class BukkitChatPlayer extends ChatPlayer {

    private final Player bukkitPlayer;
    private final String key;

    public BukkitChatPlayer(@NotNull final Player bukkitPlayer) {
        this.bukkitPlayer = Preconditions.checkNotNull(bukkitPlayer);
        this.key = this.bukkitPlayer.getUniqueId().toString();
        // Register an internal reference
        PlayerRegistry.registry.register(this);
    }

    /**
     * Get the backing {@link Player Bukkit player}
     *
     * @return Bukkit player that the player instance wraps
     */
    @NotNull public Player getBukkitPlayer() {
        return this.bukkitPlayer;
    }

    @Override public void sendMessage(@NotNull ChatMessage message) {
        TextAdapter.sendComponent(this.bukkitPlayer, message.getMessage());
    }

    @NotNull @Override public String getDisplayName() {
        return this.bukkitPlayer.getDisplayName();
    }

    @NotNull @Override public String getKey() {
        return this.key;
    }

    @Override public @NotNull String getName() {
        return this.bukkitPlayer.getName();
    }

    @Override public boolean hasPermission(@NotNull final String permission) {
        return this.bukkitPlayer.hasPermission(permission);
    }

}
