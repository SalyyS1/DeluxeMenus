# Custom Item Hooks

Hooked item providers use this material format:

```yaml
material: '<hook>-<item-id>'
```

## CraftEngine

```yaml
material: 'craftengine-namespace:item'
```

This fork compiles against CraftEngine `26.6.2` and uses a compatibility
adapter for both legacy and current CraftEngine API shapes.

## MMOItems

```yaml
material: 'mmoitems-SWORD:STEEL_SWORD'
```

The value after `mmoitems-` is `<type>:<id>`. Both values must exist in
MMOItems. DeluxeMenus asks MMOItems to build the item, then applies supported
DeluxeMenus visual overrides.

## Other Supported Hooks

- `itemsadder-<namespace:item>`
- `nexo-<item-id>`
- `oraxen-<item-id>`
- `hdb-<head-id>`
- `head-<player-name>`
- `basehead-<base64>`
- `texture-<texture-url-or-id>`

Available hooks depend on the corresponding plugin being installed and enabled
before DeluxeMenus starts.
