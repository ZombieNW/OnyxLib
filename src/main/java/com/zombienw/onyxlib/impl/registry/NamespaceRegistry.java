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
 * Global internal registry for managing all plugin namespaces.
 */
public final class NamespaceRegistry {

    private static final Map<String, OnyxNamespaceImpl> namespaces = new ConcurrentHashMap<>();

    private NamespaceRegistry() {}

    /**
     * Retrieves an existing namespace for a plugin, or creates a new one.
     */
    public static OnyxNamespace getOrCreate(Plugin plugin) {
        String pluginName = plugin.getName().toLowerCase();

        return namespaces.computeIfAbsent(pluginName, key ->
                new OnyxNamespaceImpl(plugin, key)
        );
    }

    /**
     * Retrieves a namespace without creating it.
     */
    public static OnyxNamespaceImpl getNamespace(String pluginName) {
        return namespaces.get(pluginName.toLowerCase());
    }

    /**
     * Retrieves all registered namespaces.
     */
    public static Collection<OnyxNamespaceImpl> getAllNamespaces() {
        return namespaces.values();
    }

    /**
     * Retrieves an item or block across all namespaces using its full key.
     */
    public static OnyxElement getElement(NamespacedKey key) {
        OnyxNamespaceImpl ns = namespaces.get(key.getNamespace());
        if (ns != null) {
            return ns.getElement(key.getKey());
        }
        return null;
    }

    /**
     * Locks all registered namespaces, preventing item registrations.
     */
    public static void lockAll() {
        for (OnyxNamespaceImpl namespace : namespaces.values()) {
            namespace.lock();
        }
    }

    /**
     * Resets the registry. Used during server reloads.
     */
    public static void clear() {
        namespaces.clear();
    }
}
