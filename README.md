# DeluxeMenus Documentation

Configuration reference for the extended DeluxeMenus build. This documentation
covers the menu editors, animated items, player inventory submenus, modern item
hooks, dialogs, and deployment troubleshooting.

<div class="status-row">
  <span class="status-chip ready">Build verified</span>
  <span class="status-chip">Paper 1.21.11</span>
  <span class="status-chip">Spigot compatible</span>
  <span class="status-chip">Folia compatible</span>
</div>

## Start Here

<div class="doc-grid">
  <a class="doc-card" href="#/menu-editors">
    <strong>Menu Editors</strong>
    <span>Edit menu files in-game or through the temporary browser editor.</span>
  </a>
  <a class="doc-card" href="#/player-inventory-submenus">
    <strong>Inventory Submenus</strong>
    <span>Render configurable controls in the player's lower inventory area.</span>
  </a>
  <a class="doc-card" href="#/animated-items">
    <strong>Animated Items</strong>
    <span>Build loop, reverse, ping-pong, and random item animations.</span>
  </a>
  <a class="doc-card" href="#/custom-item-hooks">
    <strong>Custom Item Hooks</strong>
    <span>Use CraftEngine, MMOItems, ItemsAdder, Nexo, Oraxen, and more.</span>
  </a>
</div>

## Quick Example

```yaml
menu_title: '<gold><bold>Server Menu</bold></gold>'
open_command: servermenu
size: 27

items:
  animated_status:
    material: RED_WOOL
    slot: 13
    display_name: '&fServer status'
    animation:
      interval: 5
      mode: ping_pong
      frames:
        online:
          material: LIME_WOOL
          display_name: '&aOnline'
        busy:
          material: YELLOW_WOOL
          display_name: '&eBusy'
```

## Compatibility

| Component | Support |
| --- | --- |
| Java | 11 or newer |
| Server software | Paper, Spigot, Folia-compatible |
| Required plugin | PlaceholderAPI |
| Compile target | Paper 1.21.11 |
| Minecraft dialogs | Minecraft 1.21.6 or newer |

Features that depend on newer Minecraft APIs use runtime compatibility checks
and fail gracefully on unsupported versions.
