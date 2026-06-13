# Actions and External GUIs

## Open Dialog

Open a registered Minecraft dialog:

```yaml
left_click_commands:
  - '[opendialog] minecraft:server_links'
```

Use a namespaced dialog key such as `namespace:key`. The dialog must already be
registered by Minecraft, a datapack, or another plugin. Dialogs require
Minecraft 1.21.6 or newer; unsupported servers log a warning instead.

## Open Another DeluxeMenus Menu

```yaml
left_click_commands:
  - '[openmenu] another_menu'
```

`[openguimenu]` is also supported.

## Open an External Plugin GUI

Player commands can open inventories owned by other plugins:

```yaml
left_click_commands:
  - '[player] chem'
```

DeluxeMenus detects the new inventory opening and releases its own holder
without closing the external inventory. A manual `[close]` action or artificial
command delay is not required.

## Open a Player Inventory Submenu

```yaml
left_click_commands:
  - '[open_gui_inventory] sub_rtp'
```

The target must be a loaded DeluxeMenus submenu.
