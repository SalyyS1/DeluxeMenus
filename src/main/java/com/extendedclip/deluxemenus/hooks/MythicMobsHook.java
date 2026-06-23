package com.extendedclip.deluxemenus.hooks;

import java.lang.reflect.Method;
import java.util.Optional;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MythicMobsHook implements InspectableItemHook {

    @Override
    public ItemStack getItem(@NotNull String... arguments) {
        if (arguments.length == 0 || arguments[0].isBlank()) {
            return new ItemStack(Material.STONE, 1);
        }

        return getMythicItem(arguments[0], 1).orElseGet(() -> new ItemStack(Material.STONE, 1));
    }

    @Override
    public boolean itemMatchesIdentifiers(@NotNull ItemStack item, @NotNull String... arguments) {
        if (arguments.length == 0 || arguments[0].isBlank()) {
            return false;
        }

        return getItemIdentifier(item).map(identifier -> identifier.equalsIgnoreCase(arguments[0])).orElse(false);
    }

    @Override
    public @NotNull Optional<String> getItemIdentifier(@NotNull ItemStack itemStack) {
        try {
            final Object manager = itemManager();
            final Method isMythicItem = manager.getClass().getMethod("isMythicItem", ItemStack.class);
            if (!Boolean.TRUE.equals(isMythicItem.invoke(manager, itemStack))) {
                return Optional.empty();
            }

            final Method getMythicTypeFromItem = manager.getClass().getMethod("getMythicTypeFromItem", ItemStack.class);
            return stringifyOptional(getMythicTypeFromItem.invoke(manager, itemStack));
        } catch (final ReflectiveOperationException exception) {
            return Optional.empty();
        }
    }

    @Override
    public String getPrefix() {
        return "mythicmobs-";
    }

    private @NotNull Optional<ItemStack> getMythicItem(final @NotNull String identifier, final int amount) {
        try {
            final Object manager = itemManager();
            final Method getItem = manager.getClass().getMethod("getItem", String.class);
            final Object optionalItem = getItem.invoke(manager, identifier);
            if (!(optionalItem instanceof Optional) || ((Optional<?>) optionalItem).isEmpty()) {
                return Optional.empty();
            }

            final Object mythicItem = ((Optional<?>) optionalItem).get();
            final Method generateItemStack = mythicItem.getClass().getMethod("generateItemStack", int.class);
            final Object abstractItemStack = generateItemStack.invoke(mythicItem, amount);
            final Method build = abstractItemStack.getClass().getMethod("build");
            final Object itemStack = build.invoke(abstractItemStack);
            if (itemStack instanceof ItemStack) {
                return Optional.of(((ItemStack) itemStack).clone());
            }
        } catch (final ReflectiveOperationException exception) {
            return Optional.empty();
        }

        return Optional.empty();
    }

    private @NotNull Optional<String> stringifyOptional(final Object value) {
        if (value instanceof Optional) {
            return ((Optional<?>) value)
                    .map(String::valueOf)
                    .filter(identifier -> !identifier.isBlank());
        }

        return Optional.ofNullable(value)
                .map(String::valueOf)
                .filter(identifier -> !identifier.isBlank());
    }

    private @NotNull Object itemManager() throws ReflectiveOperationException {
        final Class<?> mythicBukkitClass = Class.forName("io.lumine.mythic.bukkit.MythicBukkit");
        final Object mythicBukkit = mythicBukkitClass.getMethod("inst").invoke(null);
        return mythicBukkitClass.getMethod("getItemManager").invoke(mythicBukkit);
    }
}
