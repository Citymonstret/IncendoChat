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
package com.github.sauilitired.incendochat.persistence;

import com.github.sauilitired.incendochat.IncendoChat;
import com.github.sauilitired.incendochat.chat.ChannelRegistry;
import com.github.sauilitired.incendochat.chat.ChatChannel;
import com.github.sauilitired.incendochat.chat.ChatMessage;
import com.github.sauilitired.incendochat.players.ChatPlayer;
import com.google.common.base.Preconditions;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

public final class PersistenceHandler implements AutoCloseable, Runnable {

    private static final String STATEMENT_CREATE_CHAT_LOG =
        "create table if not exists chat_log("
        + "    message_id int auto_increment" + "        primary key,"
        + "    sender     varchar(36)                         null,"
        + "    timestamp  timestamp default CURRENT_TIMESTAMP null,"
        + "    channel_id varchar(128)                        null,"
        + "    message    varchar(256)                        null)";
    private static final String STATEMENT_INSERT_CHAT_LOG =
        "insert into `chat_log` (`sender`, `channel_id`, `message`) VALUES(?, ?, ?)";
    private static final String STATEMENT_CREATE_CHANNEL_MEMBERSHIP =
        "create table if not exists chat_membership ("
        + "    membership_id int auto_increment primary key,"
        + "    member        varchar(36)  null,"
        + "    channel       varchar(128) null)";
    private static final String STATEMENT_INSERT_CHANNEL_MEMBERSHIP =
        "insert into `chat_membership` (`member`, `channel`) VALUES(?, ?)";
    private static final String STATEMENT_REMOVE_CHANNEL_MEMBERSHIP =
        "delete from `chat_membership` WHERE `member` = ? AND `channel` = ?";
    private static final String STATEMENT_GET_MEMBERSHIPS =
        "select `channel` from `chat_membership` WHERE `member` = ?";

    private final HikariDataSource hikariDataSource;
    private final boolean chatLoggingEnabled;
    private final boolean channelPersistenceEnabled;
    private final Queue<ChatMessage> messageLog = new LinkedBlockingQueue<>();

    public PersistenceHandler(@Nullable final ConfigurationSection configuration) {
        if (configuration == null) {
            this.hikariDataSource = null;
            this.chatLoggingEnabled = false;
            this.channelPersistenceEnabled = false;
            return;
        }

        this.chatLoggingEnabled = configuration.getBoolean("chat-logging", false);
        this.channelPersistenceEnabled = configuration.getBoolean("channel-persistence");

        if (this.chatLoggingEnabled || this.channelPersistenceEnabled) {
            final HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setJdbcUrl(String
                .format("jdbc:mysql://%s:%d/%s", configuration.getString("mysql.host", "localhost"),
                    configuration.getInt("mysql.port", 3306),
                    configuration.getString("mysql.database", "chat")));
            hikariConfig.setUsername(configuration.getString("mysql.username", "username"));
            hikariConfig.setPassword(configuration.getString("mysql.password", ""));
            hikariConfig.addDataSourceProperty("cachePrepStmts", true);
            hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
            hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            this.hikariDataSource = new HikariDataSource(hikariConfig);
        } else {
            this.hikariDataSource = null;
        }

        if (this.chatLoggingEnabled) {
            final long delay = configuration.getLong("chat-logging-interval", 100L);

            this.useConnection(connection -> {
                try (final PreparedStatement statement = connection.prepareStatement(STATEMENT_CREATE_CHAT_LOG)) {
                    statement.executeUpdate();
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            });

            Bukkit.getScheduler().runTaskTimerAsynchronously(IncendoChat.getPlugin(IncendoChat.class),
                this, delay, delay);
        }

        if (this.channelPersistenceEnabled) {
            this.useConnection(connection -> {
                try (final PreparedStatement statement =
                    connection.prepareStatement(STATEMENT_CREATE_CHANNEL_MEMBERSHIP)) {
                    statement.executeUpdate();
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public void useConnection(@NotNull final Consumer<Connection> connectionConsumer) {
        try (final Connection connection = this.hikariDataSource.getConnection()) {
            connectionConsumer.accept(connection);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    @Override public void close() {
        if (this.hikariDataSource == null) {
            return;
        }
        if (!this.messageLog.isEmpty()) {
            // Save all pending messages on the main thread
            this.run();
        }
        this.hikariDataSource.close();
    }

    public void registerSubscription(@NotNull final ChatPlayer player, @NotNull final ChatChannel channel) {
        Preconditions.checkNotNull(player);
        Preconditions.checkNotNull(channel);
        Bukkit.getScheduler().runTaskAsynchronously(IncendoChat.getPlugin(IncendoChat.class), () -> useConnection(connection -> {
            try (final PreparedStatement preparedStatement = connection.prepareStatement(STATEMENT_INSERT_CHANNEL_MEMBERSHIP)) {
                preparedStatement.setString(1, player.getKey());
                preparedStatement.setString(2, channel.getKey());
                preparedStatement.executeUpdate();
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }));
    }

    public void registerUnsubscription(@NotNull final ChatPlayer player, @NotNull final ChatChannel channel) {
        Preconditions.checkNotNull(player);
        Preconditions.checkNotNull(channel);
        Bukkit.getScheduler().runTaskAsynchronously(IncendoChat.getPlugin(IncendoChat.class), () -> useConnection(connection -> {
            try (final PreparedStatement preparedStatement = connection.prepareStatement(STATEMENT_REMOVE_CHANNEL_MEMBERSHIP)) {
                preparedStatement.setString(1, player.getKey());
                preparedStatement.setString(2, channel.getKey());
                preparedStatement.executeUpdate();
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }));
    }

    @Override public void run() {
        if (this.messageLog.isEmpty()) {
            return;
        }
        this.useConnection(connection -> {
            try (final PreparedStatement statement = connection.prepareStatement(STATEMENT_INSERT_CHAT_LOG)) {
                ChatMessage message;
                while ((message = this.messageLog.poll()) != null) {
                    statement.setString(1, message.getSender().getKey());
                    statement.setString(2, message.getChannel().getKey());
                    statement.setString(3, message.getRawMessage());
                    statement.addBatch();
                }
                statement.executeBatch();
            } catch (final Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void loadSubscriptions(final ChatPlayer player) {
        Preconditions.checkNotNull(player);
        Bukkit.getScheduler().runTaskAsynchronously(IncendoChat.getPlugin(IncendoChat.class), () -> useConnection(connection -> {
            final Set<ChatChannel> toJoin = new HashSet<>();
            try (final PreparedStatement preparedStatement = connection.prepareStatement(STATEMENT_GET_MEMBERSHIPS)) {
                preparedStatement.setString(1, player.getKey());
                try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        ChannelRegistry.getRegistry().get(resultSet.getString("channel"))
                            .ifPresent(toJoin::add);
                    }
                }
            } catch (final Exception e) {
                e.printStackTrace();
            }
            if (!toJoin.isEmpty()) {
                Bukkit.getScheduler().runTask(IncendoChat.getPlugin(IncendoChat.class), () -> {
                    for (final ChatChannel channel : toJoin) {
                        if (channel.getChannelConfiguration().getPermission().isEmpty() ||
                            player.hasPermission(channel.getChannelConfiguration().getPermission())) {
                            player.joinChannel(channel);
                        }
                    }
                });
            }
        }));
    }

    public void logMessage(@NotNull final ChatMessage message) {
        if (this.chatLoggingEnabled) {
            this.messageLog.add(Preconditions.checkNotNull(message));
        }
    }

}
