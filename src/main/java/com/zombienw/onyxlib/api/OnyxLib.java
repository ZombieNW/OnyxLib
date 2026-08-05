package com.zombienw.onyxlib.api;

import com.zombienw.onyxlib.api.gui.OnyxGui;
import com.zombienw.onyxlib.impl.gui.OnyxGuiImpl;
import com.zombienw.onyxlib.impl.registry.NamespaceRegistry;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
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

    /**
     * Creates a new custom GUI (chest menu) builder.
     * Unlike items/blocks, guis are not registered/shared; each call returns a
     * fresh, standalone menu that can be configured and opened for players.
     * @param title The menu title.
     * @param rows The number of rows (1-6).
     * @return A new OnyxGui builder.
     */
    public static OnyxGui gui(Component title, int rows) {
        return new OnyxGuiImpl(title, rows);
    }

    /**
     * Creates a new custom GUI (chest menu) builder using a plain, non-italic title.
     * @param title The menu title.
     * @param rows The number of rows (1-6).
     * @return A new OnyxGui builder.
     */
    public static OnyxGui gui(String title, int rows) {
        return gui(Component.text(title).decoration(TextDecoration.ITALIC, false), rows);
    }
}
