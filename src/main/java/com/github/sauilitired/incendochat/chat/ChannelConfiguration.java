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

import net.kyori.text.event.ClickEvent;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Configuration for {@link ChatChannel}
 */
public final class ChannelConfiguration {

    private final String displayName;
    private final int priority;
    private final String permission;
    private final List<ChannelFormatSection> channelFormatSections;
    private final String pingFormat;

    public ChannelConfiguration(final String displayName, final int priority, final String permission,
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
            final String permission;
            if (formatSection.containsKey("permission")) {
                permission = formatSection.get("permission").toString();
            } else {
                permission = "";
            }
            sections.add(new ChannelFormatSection(text, hover, clickType, clickText, permission));
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
        private final String permission;

        public ChannelFormatSection(final String text, final String hoverText,
            final ClickEvent.Action clickAction, final String clickText, final String permission) {
            this.text = text;
            this.hoverText = hoverText;
            this.clickAction = clickAction;
            this.clickText = clickText;
            this.permission = permission;
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

        public String getPermission() {
            return permission;
        }
    }

}
