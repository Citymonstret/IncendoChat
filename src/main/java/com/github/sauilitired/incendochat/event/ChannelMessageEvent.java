package com.github.sauilitired.incendochat.event;

import com.github.sauilitired.incendochat.chat.ChatChannel;
import com.github.sauilitired.incendochat.players.ChatPlayer;
import com.google.common.base.Preconditions;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashSet;

public class ChannelMessageEvent extends ChannelEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final ChatPlayer sender;
    private String message;
    private boolean cancelled;
    private final Collection<ChatPlayer> recipients;

    public ChannelMessageEvent(@NotNull final ChatChannel channel, @NotNull final ChatPlayer sender,
        @NotNull final String message, @NotNull final Collection<ChatPlayer> recipients) {
        super(channel);
        this.sender = Preconditions.checkNotNull(sender);
        this.message = Preconditions.checkNotNull(message);
        this.recipients = new HashSet<>(Preconditions.checkNotNull(recipients));
        this.cancelled = false;
    }

    @Override @NotNull public HandlerList getHandlers() {
        return handlers;
    }

    @Override public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override public boolean isCancelled() {
        return this.cancelled;
    }

    @NotNull public ChatPlayer getSender() {
        return this.sender;
    }

    @NotNull public String getMessage() {
        return this.message;
    }

    @NotNull public Collection<ChatPlayer> getRecipients() {
        return this.recipients;
    }

    public void setMessage(@NotNull final String message) {
        this.message = Preconditions.checkNotNull(message);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
