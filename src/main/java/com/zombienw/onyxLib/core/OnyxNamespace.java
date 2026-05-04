package com.zombienw.onyxlib.core;

import com.zombienw.onyxlib.block.BlockBuilder;
import com.zombienw.onyxlib.item.ItemBuilder;
import com.zombienw.onyxlib.registry.OnyxRegistry;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * A namespace scopes all content registered by a plugin.
 *
 * <p> Every registered item, block, or other content type has a namespaced key in the form
 * {@code namespace:content_id} (example {@code "myplugin:strawberry"}).
 *
 * <p> Namespace creation is locked after {@code #freeze()} is called when OnyxLib detects
 * the owning plugin is done with {@code onEnable()}. Then an error is thrown.
 *
 * <h3>Usage</h3>
 * <pre>{@code
 * OnyxNamespace ns = OnyxLib.namespace("myplugin", this);
 *
 * ns.item("strawberry")
 *     .baseItem(Material.APPLE)
 *     .displayName("Strawberry")
 *     .register();
 *
 * ns.block("marble")
 *     .baseBlock(Material.STONE)
 *     .display(d -> d.allAround("blocks/marble.png"))
 *     .register();
 * }</pre>
 */
public final class OnyxNamespace {

    private final String id;
    private final JavaPlugin plugin;
    private final OnyxRegistry registry;

    private boolean frozen = false;

    /**
     * Package-private obtain instances via {@link OnyxLib#namespace(String, JavaPlugin)}.
     */
    OnyxNamespace(String id, JavaPlugin plugin) {
        this.id = id;
        this.plugin = plugin;
        this.registry = new OnyxRegistry(this);
    }

    // Content Builders

    /**
     * Begins building a new item with the given id.
     *
     * <p> The returned {@link ItemBuilder} is a fluent builder. Call {@link ItemBuilder#register()}
     * when done to add the item to the namespace.
     *
     * @param id     The item id; namespaced as {@code namespace:id}
     * @return a new {@link ItemBuilder}
     * @throws IllegalStateException if this namespace is frozen
     * @throws IllegalArgumentException if {@code id} is invalid
     */
    public ItemBuilder item(String id) {
        assertNotFrozen();
        validateId(id);
        return new ItemBuilder(this, id);
    }

    /**
     * Begins building a new block with the given id.
     *
     * <p> The returned {@link BlockBuilder} is a fluent builder. Call {@link BlockBuilder#register()}
     * when done to commit the block to this namespace.
     *
     * @param id    The block id; namespaced as {@code namespace:id}
     * @return a new {@link BlockBuilder}
     * @throws IllegalStateException if this namespace is frozen
     * @throws IllegalArgumentException if {@code id} is invalid
     */
    public BlockBuilder block(String id) {
        assertNotFrozen();
        validateId(id);
        return new BlockBuilder(this, id);
    }

    // TODO: ns.recipe()
    // TODO: ns.lootTable()

    // Accessors

    /**
     * Returns the namespace id (e.g. {@code "myplugin"}).
     *
     * @return namespace id
     */
    public String id() {
        return id;
    }

    /**
     * Returns the plugin that owns this namespace.
     *
     * @return owning plugin
     */
    public JavaPlugin plugin() {
        return plugin;
    }

    /**
     * Returns the content registry for this namespace.
     *
     * @return registry
     */
    public OnyxRegistry registry() {
        return registry;
    }

    /**
     * Returns {@code true} if this namespace has been frozen (no more registrations allowed).
     *
     * @return frozen state
     */
    public boolean isFrozen() {
        return frozen;
    }

    /**
     * Produces the full namespaced key string for a content id.
     *
     * <p>Example {@code key("strawberry")} -> {@code "myplugin:strawberry"}
     *
     * @param contentId the un-namespaced content id
     * @return namespaced key string
     */
    public String key(String contentId) {
        return id + ":" + contentId;
    }

    // Lifecycle

    /**
     * Freezes the namespace so no more content can be added.
     *
     * <p>OnyxLib calls this automatically after the owning pluggin is done enabling.
     */
    public void freeze() {
        this.frozen = true;
        OnyxLib.logger().info(
            "[OnyxLib] Namespace '" +
                id +
                "' frozen with " +
                registry.itemCount() +
                " item(s) and " +
                registry.blockCount() +
                " block(s)."
        );
    }

    // Helpers

    void assertNotFrozen() {
        if (frozen) {
            throw new IllegalStateException(
                "Namespace '" +
                    id +
                    "' is frozen — content may not be registered after onEnable()."
            );
        }
    }

    private static void validateId(String id) {
        if (id == null || !id.matches("[a-z0-9_]+")) {
            throw new IllegalArgumentException(
                "Content id '" +
                    id +
                    "' must be lowercase alphanumeric with underscores only."
            );
        }
    }
}
