# Troubleshooting

## Web editor link uses localhost

Set `web_editor_public_url` in `plugins/DeluxeMenus/config.yml`, then reload the
plugin. Use either a reachable direct address:

```yaml
web_editor_public_url: 'http://public-ip:8765'
```

or an HTTPS reverse proxy:

```yaml
web_editor_public_url: 'https://menus.example.com'
```

## Web editor link does not open

- Open the selected web editor port in the hosting panel and firewall.
- Confirm the host allows additional TCP ports.
- Do not reuse the Minecraft game port unless the panel explicitly supports it.
- For a reverse proxy, forward `/dm-web/*` to the embedded server.
- Confirm the session has not expired and use `/dm webeditor resume <menu>`.

A public-looking URL does not prove the port is reachable.

## A menu already has an editor session

```text
/dm webeditor resume <menu>
/dm webeditor cancel <menu>
```

Only one active web session is allowed for each menu.

## An in-game edit is not visible

Check the console for invalid values or YAML errors. Numeric fields reject
non-numeric values, and menu size must be a multiple of `9` from `9` through
`54`. Submenus support up to `36` slots.

## External plugin GUI does not open

Use the external plugin's player command directly:

```yaml
left_click_commands:
  - '[player] command'
```

This fork releases the DeluxeMenus inventory when another plugin opens its own
inventory, so `[close]` and delayed commands should not be necessary.

## Build from source

```text
./gradlew clean build
```

The complete shaded JAR is written to `build/libs/`. CI also runs the test suite
before uploading a build artifact.
