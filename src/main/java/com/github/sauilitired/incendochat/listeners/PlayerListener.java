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
package com.github.sauilitired.incendochat.listeners;

import com.github.sauilitired.incendochat.players.ChatPlayer;
import com.github.sauilitired.incendochat.players.PlayerRegistry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public final class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event) {
        final ChatPlayer player = PlayerRegistry.getRegistry().getPlayer(event.getPlayer());
        PlayerRegistry.getRegistry().deregister(player);
    }

}
