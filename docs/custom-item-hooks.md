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

When a player is viewing a menu, MMOItems receives that player context while
building the item. This allows player-scaled MMOItems data to render correctly
inside menus.

MMOItems placeholders:

```text
%deluxemenus_mmoitems_type_<slot|hand|offhand>%
%deluxemenus_mmoitems_id_<slot|hand|offhand>%
%deluxemenus_mmoitems_has_<type>:<id>%
%deluxemenus_mmoitems_amount_<type>:<id>%
%deluxemenus_mmoitems_stat_<stat>%
```

Armor slots can also be inspected with `helmet`, `chestplate`, `leggings`, and
`boots`.

## MythicMobs

```yaml
material: 'mythicmobs-ExampleItem'
```

The value after `mythicmobs-` is the MythicMobs item internal name.

MythicMobs placeholders:

```text
%deluxemenus_mythicmobs_id_<slot|hand|offhand>%
%deluxemenus_mythicmobs_has_<id>%
%deluxemenus_mythicmobs_amount_<id>%
```

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
