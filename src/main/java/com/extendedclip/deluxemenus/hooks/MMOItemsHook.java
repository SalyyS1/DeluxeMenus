package com.extendedclip.deluxemenus.hooks;

import com.extendedclip.deluxemenus.DeluxeMenus;
import com.extendedclip.deluxemenus.cache.SimpleCache;
import com.extendedclip.deluxemenus.scheduler.scheduling.schedulers.TaskScheduler;
import com.extendedclip.deluxemenus.utils.DebugLevel;
import java.math.BigDecimal;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import net.Indyuce.mmoitems.MMOItems;
import net.Indyuce.mmoitems.api.Type;
import net.Indyuce.mmoitems.api.player.PlayerData;
import net.Indyuce.mmoitems.stat.type.ItemStat;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MMOItemsHook implements InspectableItemHook, SimpleCache {

    private final Map<String, ItemStack> cache = new ConcurrentHashMap<>();
    private final DeluxeMenus plugin;
    private final TaskScheduler scheduler;

    public MMOItemsHook(final @NotNull DeluxeMenus plugin) {
        this.plugin = plugin;
        this.scheduler = plugin.getScheduler();
    }

    @Override
    public ItemStack getItem(@NotNull final Player holder, @NotNull final String... arguments) {
        return getItem(PlayerData.get(holder), arguments);
    }

    @Override
    public ItemStack getItem(@NotNull final String... arguments) {
        return getItem((PlayerData) null, arguments);
    }

    private ItemStack getItem(final PlayerData playerData, @NotNull final String... arguments) {
        if (arguments.length == 0) {
            return new ItemStack(Material.STONE, 1);
        }

        final String cacheKey = playerData == null ? arguments[0] : null;
        final ItemStack cached = cacheKey == null ? null : cache.get(cacheKey);
        if (cached != null) {
            return cached.clone();
        }

        String[] splitArgs = arguments[0].split(":", 2);
        if (splitArgs.length != 2) {
            return new ItemStack(Material.STONE, 1);
        }

        final Type itemType = MMOItems.plugin.getTypes().get(splitArgs[0].toUpperCase(Locale.ROOT));
        if (itemType == null) {
            return new ItemStack(Material.STONE, 1);
        }

        ItemStack mmoItem = null;
        try {
            mmoItem = scheduler.callSyncMethod(() -> {
                ItemStack item = playerData == null
                        ? MMOItems.plugin.getItem(itemType, splitArgs[1])
                        : MMOItems.plugin.getItem(itemType, splitArgs[1], playerData);

                if (item == null) {
                    return new ItemStack(Material.STONE, 1);
                }

                if (cacheKey != null) {
                    cache.put(cacheKey, item.clone());
                }

                return item.clone();
            }).get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            plugin.debug(DebugLevel.HIGHEST, Level.SEVERE, "Interrupted while getting MMOItem synchronously.");
        } catch (ExecutionException e) {
            plugin.debug(DebugLevel.HIGHEST, Level.SEVERE, "Error getting MMOItem synchronously.");
        }

        return mmoItem == null ? new ItemStack(Material.STONE, 1) : mmoItem.clone();
    }

    @Override
    public boolean itemMatchesIdentifiers(@NotNull ItemStack item, @NotNull String... arguments) {
        if (arguments.length == 0) {
            return false;
        }
        return getItemIdentifier(item).map(identifier -> identifier.equalsIgnoreCase(arguments[0])).orElse(false);
    }

    @Override
    public @NotNull Optional<String> getItemIdentifier(@NotNull ItemStack itemStack) {
        final String type = MMOItems.getTypeName(itemStack);
        final String id = MMOItems.getID(itemStack);
        if (type == null || type.isBlank() || id == null || id.isBlank()) {
            return Optional.empty();
        }
        return Optional.of(type + ":" + id);
    }

    @Override
    public @NotNull Optional<String> getItemType(@NotNull ItemStack itemStack) {
        return Optional.ofNullable(MMOItems.getTypeName(itemStack)).filter(type -> !type.isBlank());
    }

    @Override
    public @NotNull Optional<String> getPlayerStat(@NotNull Player player, @NotNull String statId) {
        final ItemStat<?, ?> stat = MMOItems.plugin.getStats().get(statId.toUpperCase(Locale.ROOT));
        if (stat == null) {
            return Optional.empty();
        }

        final double value = PlayerData.get(player).getStats().getStat(stat);
        if (!Double.isFinite(value)) {
            return Optional.empty();
        }

        return Optional.of(BigDecimal.valueOf(value).stripTrailingZeros().toPlainString());
    }

    @Override
    public String getPrefix() {
        return "mmoitems-";
    }

    @Override
    public void clearCache() {
        cache.clear();
    }
}
