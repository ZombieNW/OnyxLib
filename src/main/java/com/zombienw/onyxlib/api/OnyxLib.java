package com.zombienw.onyxlib.api;

import com.zombienw.onyxlib.impl.registry.NamespaceRegistry;
import org.bukkit.plugin.Plugin;

/**
 * The root for all of OnyxLib
 */
public class OnyxLib {

    private OnyxLib() {}

    /**
     * Retrieves or creates a namespace for the given plugin.
     * @param plugin The PaperMC plugin requesting the namespace.
     * @return The associated OnyxNamespace.
     */
    public static OnyxNamespace namespace(Plugin plugin) {
        return NamespaceRegistry.getOrCreate(plugin);
    }
}
