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
package com.github.sauilitired.incendochat.players;

import com.github.sauilitired.incendochat.registry.Registry;
import com.google.common.base.Preconditions;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * {@link Registry} that holds all {@link ChatPlayer players}
 * {@inheritDoc}
 */
public final class PlayerRegistry extends Registry<ChatPlayer> {

    private static final PlayerRegistry registry = new PlayerRegistry();

    public static PlayerRegistry getRegistry() {
        return registry;
    }

    private PlayerRegistry() {
        super();
    }

    private final ChatPlayer server = new ServerChatPlayer();

    @NotNull public ChatPlayer getServer() {
        return this.server;
    }

    @NotNull public ChatPlayer getPlayer(@NotNull final Player bukkitPlayer) {
        final String key = getKey(bukkitPlayer);
        ChatPlayer player = this.get(key).orElse(null);
        if (player == null) {
            player = new BukkitChatPlayer(bukkitPlayer);
            this.register(player);
        }
        return player;
    }

    @NotNull public ChatPlayer getPlayer(@NotNull final CommandSender sender) {
        if (sender instanceof Player) {
            return getPlayer((Player) sender);
        } else {
            return getServer();
        }
    }

    @NotNull public static String getKey(@NotNull final Player bukkitPlayer) {
        return Preconditions.checkNotNull(bukkitPlayer).getUniqueId().toString().toLowerCase();
    }

}
