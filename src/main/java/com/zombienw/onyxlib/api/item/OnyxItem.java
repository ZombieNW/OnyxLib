package com.zombienw.onyxlib.api.item;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.function.Consumer;

public interface OnyxItem {

    /**
     * Sets the underlying vanilla material for this item.
     * Required for the item to be created.
     * @param material The base vanilla Material.
     * @return This OnyxItem instance.
     */
    OnyxItem baseItem(Material material);

    /**
     * Sets the display name using a legacy string with no italics.
     * @param name The display name.
     * @return This OnyxItem instance.
     */
    OnyxItem displayName(String name);

    /**
     * Sets the display name using an Adventure Component.
     * @param component The display name component.
     * @return This OnyxItem instance.
     */
    OnyxItem displayName(Component component);

    /**
     * Sets the relative path to the texture file for resource pack generation.
     * @param path The path (e.g., "items/strawberry.png")
     * @return This OnyxItem instance.
     */
    OnyxItem texture(String path);

    /**
     * Provides direct access to the ItemMeta for complex modifications.
     * @param metaConsumer A consumer to manipulate the item's meta.
     * @return This OnyxItem instance.
     */
    OnyxItem itemMeta(Consumer<ItemMeta> metaConsumer);

    /**
     * Generates a single ItemStack representing this custom item.
     * Automatically applies CustomModelData and PDC tags.
     * @return A new ItemStack.
     * @throws IllegalStateException If baseItem is not defined.
     */
    ItemStack create();

    /**
     * Generates a stack of this custom item.
     * Automatically applies CustomModelData and PDC tags.
     * @param amount The size of the stack.
     * @return A new ItemStack.
     * @throws IllegalStateException If baseItem is not defined.
     */
    ItemStack create(int amount);
}
