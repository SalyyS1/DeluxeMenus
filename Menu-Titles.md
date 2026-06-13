# Menu Titles

Menu titles support legacy color codes and a compatible subset of MiniMessage
formatting.

```yaml
menu_title: '<gold><bold>Rewards</bold></gold>'
```

Supported title tags include named colors, hex colors, bold, italic,
underlined, strikethrough, obfuscated, and reset. Legacy formatting remains
supported:

```yaml
menu_title: '&6&lRewards'
```

Minecraft inventory titles are still legacy strings on Spigot-compatible
servers. Tags that require full Adventure components, such as click events,
hover events, fonts, and complex gradients, cannot be represented reliably in
an inventory title and should not be used.
