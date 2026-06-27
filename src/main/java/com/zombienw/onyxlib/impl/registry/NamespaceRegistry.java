package com.zombienw.onyxlib.impl.registry;


import com.zombienw.onyxlib.api.OnyxElement;
import com.zombienw.onyxlib.api.OnyxNamespace;
import com.zombienw.onyxlib.impl.item.OnyxItemImpl;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages the relationship between all the OnyxLib-using plugins.
 */
public final class NamespaceRegistry {

    private static final Map<String, OnyxNamespaceImpl> namespaces = new ConcurrentHashMap<>();

    private NamespaceRegistry() {}

    /**
     * Finds or create a namespace for a plugin.
     */
    public static OnyxNamespace getOrCreate(Plugin plugin) {
        String pluginName = plugin.getName().toLowerCase();

        return namespaces.computeIfAbsent(pluginName, key ->
                new OnyxNamespaceImpl(plugin, key)
        );
    }

    /**
     * Finds a namespace without creating it.
     */
    public static OnyxNamespaceImpl getNamespace(String pluginName) {
        return namespaces.get(pluginName.toLowerCase());
    }

    /**
     * Gets all registered namespaces.
     */
    public static Collection<OnyxNamespaceImpl> getAllNamespaces() {
        return namespaces.values();
    }

    /**
     * Gets an item/block from all namespaces using its full key.
     */
    public static OnyxElement getElement(NamespacedKey key) {
        OnyxNamespaceImpl ns = namespaces.get(key.getNamespace());
        if (ns != null) {
            return ns.getElement(key.getKey());
        }
        return null;
    }

    /**
     * Locks all namespaces to prevent item registration.
     */
    public static void lockAll() {
        for (OnyxNamespaceImpl namespace : namespaces.values()) {
            namespace.lock();
        }
    }

    /**
     * Resets the registry. Meant for server reloads.
     */
    public static void clear() {
        namespaces.clear();
    }
}
