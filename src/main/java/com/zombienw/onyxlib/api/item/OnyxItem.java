package com.zombienw.onyxlib.api.item;

import com.zombienw.onyxlib.api.OnyxElement;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.function.Consumer;

public interface OnyxItem extends OnyxElement {

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
     * Sets the relative path to the texture file, WITHOUT the .png extension.
     * The path is relative to assets/<namespace>/textures/ in your plugin jar.
     * <p>
     * Example: texture("items/strawberry") -> assets/myplugin/textures/items/strawberry.png
     *
     * @param path The path without extension (e.g., "items/strawberry")
     * @return This OnyxItem instance.
     */
    OnyxItem texture(String path);

    /**
     * Sets the relative path to the developer made JSON item model.
     * The path is relative to assets/<namespace>/models/ in your plugin jar.
     * @param path The path without extension (e.g., "items/cool_sword")
     * @return This OnyxItem instance.
     */
    OnyxItem model(String path);

    /**
     * Provides direct access to the ItemMeta for complex modifications.
     * @param metaConsumer A consumer to manipulate the item's meta.
     * @return This OnyxItem instance.
     */
    OnyxItem itemMeta(Consumer<ItemMeta> metaConsumer);
}
