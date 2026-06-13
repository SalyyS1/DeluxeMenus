# Player Inventory Submenus

A main menu can render a submenu inside the player's inventory area below the
top inventory.

Create a submenu file in `plugins/DeluxeMenus/sub_menu/`:

```yaml
size: 36
items:
  destination:
    material: COMPASS
    slot: 0
    display_name: '&aDestination'
```

Reference it from a normal menu:

```yaml
menu_title: '&8Travel'
size: 27
player_inventory_menu: sub_rtp
items:
  close:
    material: BARRIER
    slot: 22
    left_click_commands:
      - '[close]'
```

The submenu size may be `9`, `18`, `27`, or `36`. Items outside the configured
submenu size are not rendered.

Switch the bottom inventory submenu at runtime:

```yaml
left_click_commands:
  - '[open_gui_inventory] another_submenu'
```

The active player-inventory submenu is only carried when opening another
DeluxeMenus menu that explicitly references the same submenu. Opening an
unrelated main menu or an external inventory restores the player's real
inventory and removes the old submenu state.

Hidden inventories are restored when the player closes the menu, disconnects,
dies, or the plugin unloads. On death, real hidden contents follow the final
`keepInventory` decision and menu marker items are not dropped.
