package com.zombienw.onyxlib.api;

import com.zombienw.onyxlib.api.item.OnyxItem;
import org.bukkit.NamespacedKey;

public interface OnyxNamespace {

    /**
     * Gets the key associated with this namespace.
     * @return The underlying Bukkit NamespacedKey.
     */
    NamespacedKey getKey();

    /**
     * Retrieves an existing OnyxItem or creates a new one if it doesn't exist.
     * @param id The unique identifier for the item (e.g., "strawberry").
     * @return The OnyxItem configuration and creation wrapper.
     */
    OnyxItem item(String id);

    /**
     * Locks the namespace, preventing further item registrations or modifications.
     * This is called automatically by OnyxLib when the server finishes enabling.
     */
    void lock();
}
