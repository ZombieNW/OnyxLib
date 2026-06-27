package com.zombienw.onyxlib.api;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

/**
 * Represents any registered component in OnyxLib (Items, Blocks, etc.)
 * that can be represented as an ItemStack.
 */
public interface OnyxElement {

    /**
     * @return The string identifier of this element (e.g., "marble")
     */
    String getId();

    /**
     * @return The underlying Bukkit NamespacedKey for this element
     */
    NamespacedKey getKey();

    /**
     * Creates a single ItemStack of this element.
     * Automatically applies CustomModelData and PDC tags.
     * @return The generated ItemStack.
     * @throws IllegalStateException If baseMaterial is not defined.
     */
    ItemStack create();

    /**
     * Creates an ItemStack of this element with the specified amount.
     * Automatically applies CustomModelData and PDC tags.
     * @param amount The stack size.
     * @return The generated ItemStack.
     * @throws IllegalStateException If baseMaterial is not defined.
     */
    ItemStack create(int amount);
}
