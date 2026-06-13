# Animated Items

Animated items rotate through visual frames while preserving the parent item's
slot, requirements, and click actions.

```yaml
items:
  animated_icon:
    material: RED_WOOL
    slot: 13
    display_name: '&fAnimated icon'
    left_click_commands:
      - '[message] &aClicked'
    animation:
      interval: 5
      mode: ping_pong
      frames:
        red:
          material: RED_WOOL
          display_name: '&cRed'
        yellow:
          material: YELLOW_WOOL
          display_name: '&eYellow'
        green:
          material: LIME_WOOL
          display_name: '&aGreen'
```

## Options

- `interval`: ticks between frames, minimum `1`
- `mode`: `loop`, `reverse`, `ping_pong`, or `random`
- `frames`: ordered frame definitions

Frames inherit visual options from the parent item and may override material,
amount, dynamic amount, damage, model data, light level, display name, lore,
RGB, unbreakable, tooltip visibility, glint override, rarity, tooltip style,
and item model.

Animation tasks are scoped to each viewer and stop when the menu closes.

See `src/main/resources/animated_menu.yml` for a complete example.
