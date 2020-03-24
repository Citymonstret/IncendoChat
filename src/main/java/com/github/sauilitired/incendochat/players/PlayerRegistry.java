package com.github.sauilitired.incendochat.players;

import com.github.sauilitired.incendochat.registry.Registry;
import com.google.common.base.Preconditions;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class PlayerRegistry extends Registry<ChatPlayer> {

    public static PlayerRegistry registry = new PlayerRegistry();

    private PlayerRegistry() {
    }

    private final ChatPlayer server = new ServerChatPlayer();

    @NotNull public ChatPlayer getServer() {
        return this.server;
    }

    @NotNull public ChatPlayer getPlayer(@NotNull final Player bukkitPlayer) {
        return this.get(getKey(bukkitPlayer)).orElse(new BukkitChatPlayer(bukkitPlayer));
    }

    @NotNull private String getKey(@NotNull final Player bukkitPlayer) {
        return Preconditions.checkNotNull(bukkitPlayer).getUniqueId().toString().toLowerCase();
    }

}
