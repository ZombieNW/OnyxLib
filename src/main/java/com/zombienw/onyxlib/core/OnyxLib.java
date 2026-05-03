package com.zombienw.onyxlib.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main entry point for OnyxLib.
 *
 * <p>Plugins interact with OnyxLib by creating {@link OnyxNamespace} instances via
 * {@link #namespace(String, JavaPlugin)}. All content (items, blocks, etc.) is registered
 * through a namespace and scoped to it.
 *
 * <pre>
 * OnyxNamespace ns = OnyxLib.namespace("myplugin", this);
 * </pre>
 */
public final class OnyxLib {

    private static final Map<String, OnyxNamespace> NAMESPACES =
        new HashMap<>();
    private static Logger logger;

    private OnyxLib() {}

    /**
     * Initializes OnyxLib. Call this from your plugin's {@code onLoad()} or {@code onEnable()}.
     *
     * @param plugin    the owning pluggin; used for logging and events
     */
    public static void init(JavaPlugin plugin) {
        logger = plugin.getLogger();
        // TODO: register listeners
        logger.info("[OnyxLib] Initialized.");
    }

    /**
     * Creates or retrieves the {@link OnyxNamespace} for the given id.
     *
     * <p>Namespace ids must be lowercase alphanumeric with underscores (example {@code "myplugin})
     * The returned namespace is tied to {@code plugin} and is frozen after the plugin is enabled.
     *
     * @param id    unique namespace id; becomes prefix in {@code id:content_name} keys
     * @param plugin    the owning plugin
     * @return the namespace instances (created if it doesn't exist yet)
     * @throws IllegalArgumentException if {@code id} is invalid
     */
    public static OnyxNamespace namespace(String id, JavaPlugin plugin) {
        validateId(id);
        return NAMESPACES.computeIfAbsent(id, k -> new OnyxNamespace(plugin));
    }

    /**
     * Returns an unmodifiable view of all registered namespaces.
     *
     * @return map of namespaces id -> namespace
     */
    public static Map<String, OnyxNamespace> namespaces() {
        return Collections.unmodifiableMap(NAMESPACES);
    }

    /**
     * Returns the namespace with the given id, or {@code null} if none exists.
     *
     * @param id     namespace id
     * @return namespace or {@code null}
     */
    public static OnyxNamespace getNameSpace(String id) {
        return NAMESPACES.get(id);
    }

    // Helpers
    private static void validateId(String id) {
        if (id == null || !id.matches("[a-z0-9_]+")) {
            throw new IllegalArgumentException(
                "Namespace id '" +
                    id +
                    "' must be lowercase alphanumeric with underscores only."
            );
        }
    }

    static Logger logger() {
        return logger;
    }
}
