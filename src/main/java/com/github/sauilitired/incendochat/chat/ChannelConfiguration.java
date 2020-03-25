package com.github.sauilitired.incendochat.chat;

import net.kyori.text.event.ClickEvent;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChannelConfiguration {

    private final String displayName;
    private final int priority;
    private final String permission;
    private final List<ChannelFormatSection> channelFormatSections;
    private final String pingFormat;

    private ChannelConfiguration(final String displayName, final int priority, final String permission,
        final List<ChannelFormatSection> channelFormatSections, final String pingFormat) {
        this.displayName = displayName;
        this.priority = priority;
        this.permission = permission;
        this.channelFormatSections = channelFormatSections;
        this.pingFormat = pingFormat;
    }

    public static ChannelConfiguration parse(final ConfigurationSection section) {
        final String displayName = section.getString("display_name", "");
        final List<ChannelFormatSection> sections = new ArrayList<>();
        for (final Map<?, ?> formatSection : section.getMapList("format")) {
            final String text;
            if (formatSection.containsKey("text")) {
                text = formatSection.get("text").toString();
            } else {
                text = "";
            }
            final String hover;
            if (formatSection.containsKey("hover")) {
                hover = formatSection.get("hover").toString();
            } else {
                hover = "";
            }
            final ClickEvent.Action clickType;
            if (formatSection.containsKey("click_type")) {
                clickType = ClickEvent.Action.valueOf(formatSection.get("click_type").toString());
            } else {
                clickType = null;
            }
            final String clickText;
            if (formatSection.containsKey("click_text")) {
                clickText = formatSection.get("click_text").toString();
            } else {
                clickText = "";
            }
            sections.add(new ChannelFormatSection(text, hover, clickType, clickText));
        }
        final String permission = section.getString("permission", "");
        final int priority = section.getInt("priority", -1);
        final String pingFormat = section.getString("ping_format", "");
        return new ChannelConfiguration(displayName, priority, permission, sections, pingFormat);
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public int getPriority() {
        return this.priority;
    }

    public String getPermission() {
        return this.permission;
    }

    public List<ChannelFormatSection> getChannelFormatSections() {
        return this.channelFormatSections;
    }

    public String getPingFormat() {
        return this.pingFormat;
    }

    public static class ChannelFormatSection {

        private final String text;
        private final String hoverText;
        private final ClickEvent.Action clickAction;
        private final String clickText;

        public ChannelFormatSection(final String text, final String hoverText,
            final ClickEvent.Action clickAction, final String clickText) {
            this.text = text;
            this.hoverText = hoverText;
            this.clickAction = clickAction;
            this.clickText = clickText;
        }

        public String getText() {
            return text;
        }

        public String getHoverText() {
            return hoverText;
        }

        public ClickEvent.Action getClickAction() {
            return clickAction;
        }

        public String getClickText() {
            return clickText;
        }
    }

}
