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
package com.github.sauilitired.incendochat.chat;

import com.github.sauilitired.incendochat.IncendoChat;
import com.github.sauilitired.incendochat.players.ChatPlayer;
import org.jetbrains.annotations.NotNull;

public class StaticChatChannel extends ChatChannel {

    public StaticChatChannel(@NotNull final String key,
        @NotNull final ChannelConfiguration channelConfiguration) {
        super(key, channelConfiguration);
    }

    @Override public boolean isValid(@NotNull ChatPlayer player) {
        return true;
    }

    @Override public boolean registerSubscriber(@NotNull ChatPlayer player) {
        if (super.registerSubscriber(player)) {
            IncendoChat.getPlugin(IncendoChat.class).getPersistenceHandler().registerSubscription(player, this);
            return true;
        }
        return false;
    }

    @Override public boolean deregisterSubscriber(@NotNull ChatPlayer player) {
        if (super.deregisterSubscriber(player)) {
            IncendoChat.getPlugin(IncendoChat.class).getPersistenceHandler().registerUnsubscription(player, this);
            return true;
        }
        return false;
    }

}
