# IncendoChat

**Work in progress!** 

Very simple channel based chat plugin. Will support the following features:
- Joinable channels, with permissions, and an API that allows you to add custom restrictions
- Chat formatting, supporting vault and PlaceholderAPI 
- JSON message formatting. Click to message a channel, click to message a player, etc. 
- @player ping notifications
- API to interact with chat players, channels, etc, plus events for: channel join,
 channel leave, channel creation and channel messages
- (Optional) chat logging

Other features may be added further on.

This plugin will not:
- Handle mutes, punishments, etc. Use a punishment plugin for this.
- Handle swear blocking, spam blocking etc. I recommend [ChatControl](https://www.spigotmc.org/resources/chatcontrol™-the-ultimate-chat-plugin-500-000-downloads-1-2-5-1-15-2.271/)
and [ChatEx](https://dev.bukkit.org/projects/chatex) for this functionality. IncendoChat
is compatible with plugins such as these, that alter chat messages, and will respect
their rules.

## Requirements
- You need to have [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/) installed.
- You need to run (at least) Java 8.
- The plugin is built for Spigot 1.15+. It may work with earlier versions, but they're entirely unsupported.

## Commands
- **/ch help** - Display the help text (Permission: `incendochat.channel`)
- **/ch join \<channel\>** - Join a channel (Permission: `incendochat.channel.join`)
- **/ch leave \<channel\>** - Leave a channel (Permission: `incendochat.channel.leave`)
- **/ch set \<channel\>** - Make the channel your active channel (Permission: `incendochat.channel.set`)
- **/ch send \<channel\> \<msg...\>** - Send a message to a channel you're in (Permission: `incendochat.channel.send`)
- **/ch list** - List all channels (Permission: `incendochat.channel.list`)

## Configuration Reference

```yaml
# Pre-configured chat channels
channels:
  # The global (default channel)
  global:
    # Name that shows up in the format, and in /ch list
    display_name: 'Global'
    # Format fragments. All of them are joined together to form
    # a single message
    format:
        # The text that is sent. Supports PlaceholderAPI placeholders
        # + %channel% and %channel_displayname%, and & color codes
      - text: '&c[&6%channel%&c] '
        # Message sent on text hover. Set to '' to disable 
        hover: '&6Click to switch to this channel!'
        # Action performed on click. Set to '' to disable
        click_type: 'SUGGEST_COMMAND'
        # Text inserted on click. Set to '' to disable
        click_text: '/channel set %channel_id%'
      - text: '&c[&6%player_uuid%&c]'
        # Only players with the specified permission will receive this
        # fragment
        permission: 'incendochat.admin'
      - text: '%vault_prefix% %player_displayname%&7: '
        hover: '&6Click to message this player!'
        click_type: 'SUGGEST_COMMAND'
        click_text: '/msg %player_name% '
      - text: '&r%message%'
    # Permission needed to join the channel. Set to '' to disable
    permission: ''
    # @username ping replacement text. Set to '' to disable
    ping_format: '&c@%name%&r'
    # The priority determines which channel will
    # be the active channel, when channels are
    # re-calculates
    priority: -1
persistence:
  # Whether chat should be logged to the database
  chat-logging: false
  chat-logging-interval: 100 # every 100th tick
  # Whether channel memberships should persist
  channel-persistence: false
  # MySQL settings
  mysql:
    host: 'localhost'
    port: 3306
    username: 'username'
    password: 'password'
    database: 'chat'
# Configurable messages
messages:
  list-header: '&6&lIncendoChat&8> &7Channel List'
  list-header-active: '&eActive Channels (Click to leave):'
  list-header-available: '&eAvailable Channels (Click to join):'
  list-item: '&7- &6%channel%'
  list-item-current: '&7- &6%channel% (Current)'
  list-hover-join: '&6Click to join the channel'
  list-hover-leave: '&6Click to leave the channel'
  active-channel-set: '&6&lIncendoChat&8> &7Your active channel was set to: %channel%'
  channel-joined: '&6&lIncendoChat&8> &7You joined channel: %channel%'
  channel-left: '&6&lIncendoChat&8> &7You left channel: %channel%'
```

## Maven Repo [![](https://jitpack.io/v/Sauilitired/IncendoChat.svg)](https://jitpack.io/#Sauilitired/IncendoChat)

We use [JitPack](https://jitpack.io/#Sauilitired/IncendoCha)

Repository:
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Dependency:
```xml
<dependency>
    <groupId>com.github.Sauilitired</groupId>
    <artifactId>IncendoChat</artifactId>
    <version>master-XXXXXXXX</version>
</dependency>
```

**Note: Use the version shown on the badge to stay up to date!**
