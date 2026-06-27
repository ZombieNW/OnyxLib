package com.zombienw.onyxlib.impl.registry;


import com.zombienw.onyxlib.api.OnyxElement;
import com.zombienw.onyxlib.api.OnyxNamespace;
import com.zombienw.onyxlib.impl.item.OnyxItemImpl;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.*;
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
    public static OnyxNamespace getOrCreate(@NonNull Plugin plugin) {
        Objects.requireNonNull(plugin, "Plugin cannot be null");

        String pluginName = plugin.getName().toLowerCase(Locale.ROOT);
        return namespaces.computeIfAbsent(pluginName, key -> new OnyxNamespaceImpl(plugin, key));
    }

    /**
     * Finds a namespace without creating it.
     */
    public static @Nullable OnyxNamespaceImpl getNamespace(@NonNull String pluginName) {
        Objects.requireNonNull(pluginName, "Plugin name cannot be null");
        return namespaces.get(pluginName.toLowerCase(Locale.ROOT));
    }

    /**
     * Gets all registered namespaces.
     */
    public static @NonNull Collection<OnyxNamespaceImpl> getAllNamespaces() {
        return Collections.unmodifiableCollection(namespaces.values());
    }

    /**
     * Gets an item/block from all namespaces using its full key.
     */
    public static @Nullable OnyxElement getElement(@NonNull NamespacedKey key) {
        Objects.requireNonNull(key, "NamespacedKey cannot be null");
        OnyxNamespaceImpl ns = namespaces.get(key.getNamespace());
        return ns != null ? ns.getElement(key.getKey()) : null;
    }

    /**
     * Locks all namespaces to prevent item registration.
     */
    public static synchronized void lockAll() {
        for (OnyxNamespaceImpl namespace : namespaces.values()) {
            namespace.lock();
        }
    }

    /**
     * Resets the registry. Meant for server reloads.
     */
    public static synchronized void clear() {
        namespaces.clear();
    }
}
