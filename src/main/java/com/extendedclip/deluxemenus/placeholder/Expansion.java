package com.extendedclip.deluxemenus.placeholder;

import com.extendedclip.deluxemenus.DeluxeMenus;
import com.extendedclip.deluxemenus.hooks.InspectableItemHook;
import com.extendedclip.deluxemenus.hooks.ItemHook;
import com.extendedclip.deluxemenus.menu.Menu;
import com.extendedclip.deluxemenus.menu.options.MenuOptions;
import com.extendedclip.deluxemenus.persistentmeta.DataType;
import com.extendedclip.deluxemenus.utils.VersionHelper;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class Expansion extends PlaceholderExpansion {

    private final DeluxeMenus plugin;

    public Expansion(@NotNull final DeluxeMenus instance) {
        this.plugin = instance;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @NotNull String getIdentifier() {
        return plugin.getName().toLowerCase();
    }

    @Override
    public @NotNull String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public @NotNull List<String> getPlaceholders() {
        return List.of(
                "%deluxemenus_is_in_menu%",
                "%deluxemenus_opened_menu%",
                "%deluxemenus_last_menu%",
                "%deluxemenus_mmoitems_type_<slot|hand|offhand>%",
                "%deluxemenus_mmoitems_id_<slot|hand|offhand>%",
                "%deluxemenus_mmoitems_has_<type>:<id>%",
                "%deluxemenus_mmoitems_amount_<type>:<id>%",
                "%deluxemenus_mmoitems_stat_<stat>%",
                "%deluxemenus_mythicmobs_id_<slot|hand|offhand>%",
                "%deluxemenus_mythicmobs_has_<id>%",
                "%deluxemenus_mythicmobs_amount_<id>%",
                "%deluxemenus_meta_has_value_<key>_[type]%",
                "%deluxemenus_meta_<key>_<type>_[default-value]%"
        );
    }

    @Override
    public @Nullable String onRequest(final OfflinePlayer offlinePlayer, @NotNull final String input) {
        if (offlinePlayer == null || !offlinePlayer.isOnline()) {
            return null;
        }

        final Player onlinePlayer = offlinePlayer.getPlayer();
        if (onlinePlayer == null) {
            return null;
        }

        final String parsedInput = PlaceholderAPI.setBracketPlaceholders(onlinePlayer, input);
        final String parsedInputLower = parsedInput.toLowerCase();

        switch (parsedInputLower) {
            case "is_in_menu": {
                return getBooleanAsString(Menu.getMenuHolder(onlinePlayer).isPresent());
            }
            case "opened_menu": {
                return Menu.getOpenMenu(onlinePlayer).map(Menu::options).map(MenuOptions::name).orElse("");
            }
            case "last_menu": {
                return Menu.getLastMenu(onlinePlayer).map(Menu::options).map(MenuOptions::name).orElse("");
            }
        }

        if (parsedInputLower.startsWith("mmoitems_")) {
            return inspectHookPlaceholder("mmoitems", parsedInput.substring("mmoitems_".length()), onlinePlayer);
        }

        if (parsedInputLower.startsWith("mythicmobs_")) {
            return inspectHookPlaceholder("mythicmobs", parsedInput.substring("mythicmobs_".length()), onlinePlayer);
        }

        if (!parsedInputLower.startsWith("meta_")) {
            return null;
        }

        if (!VersionHelper.IS_PDC_VERSION || plugin.getPersistentMetaHandler() == null) {
            return null;
        }

        // %deluxemenus_meta_has_value_<key>_[type]%
        if (parsedInputLower.startsWith("meta_has_value_")) {
            final String hasValueInput = parsedInput.substring(15);
            final String[] hasValueParts = hasValueInput.split("_", 2);

            if (hasValueParts.length < 1 || hasValueParts.length > 2) {
                return null;
            }

            final NamespacedKey key = plugin.getPersistentMetaHandler().getKey(hasValueParts[0]);
            if (key == null) {
                return getBooleanAsString(false);
            }

            if (hasValueParts.length == 1) {
                return getBooleanAsString(plugin.getPersistentMetaHandler().hasMetaValue(onlinePlayer, key));
            }

            final DataType<?, ?> type = DataType.getSupportedTypeByName(hasValueParts[1]);
            if (type == null) {
                return getBooleanAsString(false);
            }

            return getBooleanAsString(plugin.getPersistentMetaHandler().hasMetaValue(onlinePlayer, key, type));
        }

        // %deluxemenus_meta_<key>_<type>_[default-value]%
        final String getValueInput = parsedInput.substring(5);

        if (!getValueInput.contains("_")) {
            return null;
        }

        final String[] parts = getValueInput.split("_", 3);

        if (parts.length < 2) {
            return null;
        }

        final NamespacedKey key = plugin.getPersistentMetaHandler().getKey(parts[0]);
        if (key == null) {
            return getBooleanAsString(false);
        }

        final DataType<?, ?> type = DataType.getSupportedTypeByName(parts[1]);
        if (type == null) {
            return getBooleanAsString(false);
        }

        final Object result = plugin.getPersistentMetaHandler().getMetaValue(onlinePlayer, key, type);

        if (result != null) {
            return String.valueOf(result);
        }

        // return the default value
        return parts.length > 2 ? parts[2] : "";
    }

    private @NotNull String getBooleanAsString(final boolean value) {
        return value ? PlaceholderAPIPlugin.booleanTrue() : PlaceholderAPIPlugin.booleanFalse();
    }

    private @Nullable String inspectHookPlaceholder(
            final @NotNull String hookName,
            final @NotNull String input,
            final @NotNull Player player
    ) {
        final Optional<ItemHook> optionalHook = plugin.getItemHook(hookName);
        if (optionalHook.isEmpty() || !(optionalHook.get() instanceof InspectableItemHook)) {
            return "";
        }

        final InspectableItemHook hook = (InspectableItemHook) optionalHook.get();
        final String lowerInput = input.toLowerCase(Locale.ROOT);

        if (lowerInput.startsWith("has_")) {
            return getBooleanAsString(hook.hasItem(player, input.substring(4)));
        }

        if (lowerInput.startsWith("amount_")) {
            return String.valueOf(hook.countItems(player, input.substring(7)));
        }

        if (lowerInput.startsWith("stat_")) {
            return hook.getPlayerStat(player, input.substring(5)).orElse("");
        }

        if (lowerInput.startsWith("type_")) {
            return getPlayerItem(player, input.substring(5))
                    .flatMap(hook::getItemType)
                    .orElse("");
        }

        if (lowerInput.startsWith("id_")) {
            return getPlayerItem(player, input.substring(3))
                    .flatMap(hook::getItemIdentifier)
                    .orElse("");
        }

        return null;
    }

    private @NotNull Optional<ItemStack> getPlayerItem(final @NotNull Player player, final @NotNull String source) {
        final String normalized = source.toLowerCase(Locale.ROOT);
        switch (normalized) {
            case "hand":
            case "mainhand":
            case "main_hand":
                return Optional.of(player.getInventory().getItemInMainHand());
            case "offhand":
            case "off_hand":
                return Optional.of(player.getInventory().getItemInOffHand());
            case "helmet":
                return Optional.ofNullable(player.getInventory().getHelmet());
            case "chestplate":
                return Optional.ofNullable(player.getInventory().getChestplate());
            case "leggings":
                return Optional.ofNullable(player.getInventory().getLeggings());
            case "boots":
                return Optional.ofNullable(player.getInventory().getBoots());
            default:
                try {
                    return Optional.ofNullable(player.getInventory().getItem(Integer.parseInt(normalized)));
                } catch (final NumberFormatException exception) {
                    return Optional.empty();
                }
        }
    }
}
