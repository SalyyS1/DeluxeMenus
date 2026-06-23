package com.extendedclip.deluxemenus.hooks;

import java.util.Optional;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface InspectableItemHook extends ItemHook {

    @NotNull Optional<String> getItemIdentifier(@NotNull ItemStack itemStack);

    default @NotNull Optional<String> getItemType(@NotNull ItemStack itemStack) {
        return Optional.empty();
    }

    default int countItems(@NotNull Player player, @NotNull String identifier) {
        int amount = 0;
        amount += countItems(player.getInventory().getStorageContents(), identifier);
        amount += countItems(player.getInventory().getArmorContents(), identifier);
        amount += countItem(player.getInventory().getItemInOffHand(), identifier);
        return amount;
    }

    default boolean hasItem(@NotNull Player player, @NotNull String identifier) {
        return countItems(player, identifier) > 0;
    }

    default @NotNull Optional<String> getPlayerStat(@NotNull Player player, @NotNull String statId) {
        return Optional.empty();
    }

    private int countItems(@NotNull ItemStack[] itemStacks, @NotNull String identifier) {
        int amount = 0;
        for (final ItemStack itemStack : itemStacks) {
            amount += countItem(itemStack, identifier);
        }
        return amount;
    }

    private int countItem(ItemStack itemStack, @NotNull String identifier) {
        if (itemStack == null || !itemMatchesIdentifiers(itemStack, identifier)) {
            return 0;
        }
        return Math.max(1, itemStack.getAmount());
    }
}
