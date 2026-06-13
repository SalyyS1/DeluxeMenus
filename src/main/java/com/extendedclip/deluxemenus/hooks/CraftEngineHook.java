package com.extendedclip.deluxemenus.hooks;

import com.extendedclip.deluxemenus.cache.SimpleCache;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CraftEngineHook implements ItemHook, SimpleCache {

    private static final ItemStack EMPTY = new ItemStack(Material.STONE);
    private final Map<String, ItemStack> cache = new ConcurrentHashMap<>();
    private final Class<?> itemsApi;
    private final Class<?> keyType;

    public CraftEngineHook() {
        this.itemsApi = loadClass("net.momirealms.craftengine.bukkit.api.CraftEngineItems");
        this.keyType = loadClass("net.momirealms.craftengine.core.util.Key");
    }

    @Override
    public void clearCache() {
        cache.clear();
    }

    @Override
    public ItemStack getItem(@NotNull String... arguments) {
        if (arguments.length == 0) return EMPTY.clone();
        final String namespaceId = arguments[0];
        final ItemStack cached = cache.get(namespaceId);
        if (cached != null) return cached.clone();

        final ItemStack result = buildItem(findItem(namespaceId), null);
        if (result == null) return EMPTY.clone();
        cache.put(namespaceId, result);
        return result.clone();
    }

    @Override
    public ItemStack getItem(@NotNull Player holder, @NotNull String... arguments) {
        if (arguments.length == 0) return EMPTY.clone();
        final ItemStack result = buildItem(findItem(arguments[0]), holder);
        return result == null ? EMPTY.clone() : result;
    }

    @Override
    public boolean itemMatchesIdentifiers(@NotNull ItemStack item, @NotNull String... arguments) {
        if (arguments.length == 0 || itemsApi == null) return false;

        final Object latestId = invokeStatic(itemsApi, "getCustomItemId", item);
        if (latestId != null) {
            return latestId.toString().equals(arguments[0]);
        }

        final Object customItem = invokeStatic(itemsApi, "byItemStack", item);
        final Object legacyId = invoke(customItem, "id");
        return legacyId != null && legacyId.toString().equals(arguments[0]);
    }

    @Override
    public String getPrefix() {
        return "craftengine-";
    }

    private Object findItem(final String namespaceId) {
        if (itemsApi == null) return null;

        final Object stringResult = invokeStatic(itemsApi, "byId", namespaceId);
        if (stringResult != null || keyType == null) return stringResult;

        final Object key = invokeStatic(keyType, "of", namespaceId);
        return invokeStatic(itemsApi, "byId", key);
    }

    private ItemStack buildItem(final Object customItem, final Player player) {
        if (customItem == null) return null;

        if (player != null) {
            final Object latest = invoke(customItem, "buildBukkitItem", player);
            if (latest instanceof ItemStack) return (ItemStack) latest;

            final Object adaptedPlayer = adaptPlayer(player);
            final Object legacy = invoke(customItem, "buildItemStack", adaptedPlayer);
            if (legacy instanceof ItemStack) return (ItemStack) legacy;
        }

        final Object latest = invoke(customItem, "buildBukkitItem");
        if (latest instanceof ItemStack) return (ItemStack) latest;

        final Object legacy = invoke(customItem, "buildItemStack");
        return legacy instanceof ItemStack ? (ItemStack) legacy : null;
    }

    private Object adaptPlayer(final Player player) {
        final Class<?> adaptor = loadClass("net.momirealms.craftengine.bukkit.api.BukkitAdaptor");
        final Object latest = invokeStatic(adaptor, "adapt", player);
        if (latest != null) return latest;

        final Class<?> legacyAdaptor = loadClass("net.momirealms.craftengine.bukkit.api.BukkitAdaptors");
        return invokeStatic(legacyAdaptor, "adapt", player);
    }

    private Object invokeStatic(final Class<?> type, final String name, final Object... arguments) {
        if (type == null) return null;
        return invokeMethod(type, null, name, arguments);
    }

    private Object invoke(final Object target, final String name, final Object... arguments) {
        if (target == null) return null;
        return invokeMethod(target.getClass(), target, name, arguments);
    }

    private Object invokeMethod(final Class<?> type, final Object target, final String name, final Object... arguments) {
        final Method method = Arrays.stream(type.getMethods())
                .filter(candidate -> candidate.getName().equals(name))
                .filter(candidate -> Modifier.isStatic(candidate.getModifiers()) == (target == null))
                .filter(candidate -> parametersMatch(candidate.getParameterTypes(), arguments))
                .findFirst()
                .orElse(null);
        if (method == null) return null;

        try {
            return method.invoke(target, arguments);
        } catch (ReflectiveOperationException | IllegalArgumentException | LinkageError ignored) {
            return null;
        }
    }

    private boolean parametersMatch(final Class<?>[] parameterTypes, final Object[] arguments) {
        if (parameterTypes.length != arguments.length) return false;
        for (int index = 0; index < parameterTypes.length; index++) {
            if (arguments[index] != null && !parameterTypes[index].isAssignableFrom(arguments[index].getClass())) {
                return false;
            }
        }
        return true;
    }

    private Class<?> loadClass(final String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException | LinkageError ignored) {
            return null;
        }
    }
}
