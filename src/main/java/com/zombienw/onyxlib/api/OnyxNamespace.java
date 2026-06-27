package com.zombienw.onyxlib.api;

import com.zombienw.onyxlib.api.block.OnyxBlock;
import com.zombienw.onyxlib.api.item.OnyxItem;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public interface OnyxNamespace {

    /**
     * Gets the key associated with this namespace.
     * @return The underlying Bukkit NamespacedKey.
     */
    NamespacedKey getKey();

    /**
     * Creates or looks up an OnyxItem.
     * @param id The unique identifier for the item (e.g., "strawberry").
     * @return The OnyxItem configuration and creation wrapper.
     */
    OnyxItem item(String id);

    /**
     * Checks the given ItemStack and matches it to a registered OnyxItem if it exists.
     * @param stack The ItemStack to check.
     * @return The matching OnyxItem or {@code null} if not found.
     */
    OnyxItem matchItem(ItemStack stack);

    /**
     * Creates or looks up an OnyxBlock.
     * @param id The unique identifier for the item (e.g., "marble").
     * @return The OnyxBlock configuration and creation wrapper.
     */
    OnyxBlock block(String id);

    /**
     * Checks the given block to determine if it is tracking an OnyxBlock.
     * @param block The physical Block to check.
     * @return The matching OnyxBlock or {@code null} if not found.
     */
    OnyxBlock matchBlock(Block block);
}
