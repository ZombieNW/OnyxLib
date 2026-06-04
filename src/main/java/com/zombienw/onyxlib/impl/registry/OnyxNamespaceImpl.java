package com.zombienw.onyxlib.impl.registry;

import com.zombienw.onyxlib.api.OnyxNamespace;
import com.zombienw.onyxlib.api.item.OnyxItem;
import com.zombienw.onyxlib.impl.item.OnyxItemImpl;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OnyxNamespaceImpl implements OnyxNamespace {

    private final Plugin plugin;
    private final NamespacedKey namespaceKey;
    private final NamespacedKey itemPdcKey;
    private boolean isLocked = false;

    private final Map<String, OnyxItemImpl> registeredItems = new ConcurrentHashMap<>();

    public OnyxNamespaceImpl(Plugin plugin, String namespaceId) {
        this.plugin = plugin;
        this.namespaceKey = new NamespacedKey(plugin, namespaceId);
        this.itemPdcKey = new NamespacedKey(plugin, "onyx_item_id");
    }

    @Override
    public NamespacedKey getKey() {
        return this.namespaceKey;
    }

    public Plugin getPlugin() { return this.plugin; }

    @Override
    public OnyxItem item(String id) {
        if (this.isLocked) {
            throw new IllegalStateException(
                    "Cannot register or modify item '" + id + "'. The namespace for " +
                    plugin.getName() + " is currently locked."
            );
        }

        return this.registeredItems.computeIfAbsent(id, k -> {
            NamespacedKey itemKey = new NamespacedKey(this.plugin, id);

            return new OnyxItemImpl(id, itemKey, this.itemPdcKey);
        });
    }

    @Override
    public void lock() {
        if (this.isLocked) return;
        this.isLocked = true;

        // warn about incorrectly registered items
        for (OnyxItemImpl item : registeredItems.values()) {
            if (item.getBaseMaterial() == null) {
                plugin.getLogger().warning("Item '" + item.getId() + "' was registered but never assigned a base material. It will be unusable.");
            }
        }
    }

    /**
     * Used for pack generation
     */
    public Collection<OnyxItemImpl> getItems() {
        return this.registeredItems.values();
    }

    /**
     * Used for item search
     */
    public OnyxItemImpl getItem(String id) {
        return this.registeredItems.get(id);
    }
}
