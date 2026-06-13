# Menu Editors

## In-game Editor

Open an editor preview:

```text
/dm edit <menu>
```

Permission: `deluxemenus.edit`

Click a slot to edit it. The slot editor supports material, amount, display
name, lore, model data, item flags, placeholder updates, priority, and click
commands. Options that require text input ask for the value in chat. Use `|`
between list entries and type `cancel` to cancel the prompt.

Every successful edit is written to the menu YAML file and the edited menu is
reloaded immediately. File writes are atomic, so an interrupted save does not
leave a partially written menu file.

## Web Editor

Permission: `deluxemenus.webeditor`

```text
/dm webeditor <menu>
/dm webeditor list
/dm webeditor resume <menu>
/dm webeditor cancel <menu>
/dm webeditor local <menu> [host:port]
```

Only one active session is allowed per menu. Sessions expire after 60 minutes.
Saving changes writes the YAML file and reloads the menu in-game.

The editor runs inside the Minecraft server process. It can be opened from a
player or the console, but browsers must be able to reach its HTTP port.

### Direct Port

1. Open a separate TCP port in the hosting panel, such as `8765`.
2. Set a public address in `config.yml`:

```yaml
web_editor_public_url: 'http://185.207.166.79:8765'
```

3. Reload DeluxeMenus and run:

```text
/dm webeditor advanced_menu
```

The Minecraft game port and web editor port are separate. Adding a port to the
generated URL does not open that port in the hosting firewall.

### HTTPS Reverse Proxy

Proxy a public domain to the server's internal port `8765`, then configure:

```yaml
web_editor_public_url: 'https://menus.example.com'
```

Do not add a path prefix. The proxy must forward `/dm-web/*` to the embedded
web editor. With a reverse proxy, the generated public URL keeps HTTPS and does
not expose the internal port.

Treat session links as secrets. Anyone with an active link can edit that menu.
