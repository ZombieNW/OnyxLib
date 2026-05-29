package com.zombienw.onyxlib.registry;

import com.zombienw.onyxlib.block.OnyxBlock;
import com.zombienw.onyxlib.core.OnyxNamespace;
import com.zombienw.onyxlib.item.OnyxItem;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Stores content registered by a {@link OnyxNamespace}
 *
 * <p>The registry is keyed on un-namespaced content id's. External code should look
 * things up via {@link OnyxNamespace} and not this.
 */
public final class OnyxRegistry {

    private final OnyxNamespace namespace;

    /** Registered custom items, keyed on un-namespaced id. */
    private final Map<String, OnyxItem> items = new LinkedHashMap<>();

    /** Registered custom blocks, keyed on un-namespaced id. */
    private final Map<String, OnyxBlock> blocks = new LinkedHashMap<>();

    // TODO: recipes
    // TODO: loot tables

    /**
     * Package-private created by {@link OnyxNamespace}.
     */
    public OnyxRegistry(OnyxNamespace namespace) {
        this.namespace = namespace;
    }

    // Items

    /**
     * Registers a custom item. Called by {@link dev.onyxlib.item.ItemBuilder#register()}.
     * @param item item to register
     * @throws IllegalArgumentException if an item with the same id is already registered
     */
    public void registerItem(OnyxItem item) {
        String id = item.id();
        if (items.containsKey(id)) {
            throw new IllegalArgumentException(
                "Item '" +
                    namespace.key(id) +
                    "' is already registered in namespace '" +
                    namespace.id() +
                    "'."
            );
        }
        items.put(id, item);

        // TODO: register custom model data/resource pack entry
        // TODO: register PDC marker on the item
    }

    /**
     * Returns the custom item with the given un-namespaced id, if any.
     *
     * @param id un-namespaced item id
     * @return optional item
     */
    public Optional<OnyxItem> findItem(String id) {
        return Optional.ofNullable(items.get(id));
    }

    /**
     * Returns an unmodifiable view of all registered items.
     *
     * @return items
     */
    public Collection<OnyxItem> items() {
        return Collections.unmodifiableCollection(items.values());
    }

    /**
     * Returns the number of registered items.
     *
     * @return item count
     */
    public int itemCount() {
        return items.size();
    }

    // Blocks

    /**
     * Registers a custom block. Called by {@link dev.onyxlib.block.BlockBuilder#register()}.
     *
     * @param block block to register
     * @throws IllegalArgumentException if a block with the same id is already registered
     */
    public void registerBlock(OnyxBlock block) {
        String id = block.id();
        if (blocks.containsKey(id)) {
            throw new IllegalArgumentException(
                "Block '" +
                    namespace.key(id) +
                    "' is already registered in namespace '" +
                    namespace.id() +
                    "'."
            );
        }
        blocks.put(id, block);
        // TODO: register block display entity model
        // TODO: link block to its base Material for event routing
    }

    /**
     * Returns the custom block with the given un-namespaced id, if any.
     *
     * @param id un-namespaced block id
     * @return optional block
     */
    public Optional<OnyxBlock> findBlock(String id) {
        return Optional.ofNullable(blocks.get(id));
    }

    /**
     * Returns an unmodifiable view of all registered blocks.
     *
     * @return blocks
     */
    public Collection<OnyxBlock> blocks() {
        return Collections.unmodifiableCollection(blocks.values());
    }

    /**
     * Returns the number of registered blocks.
     *
     * @return block count
     */
    public int blockCount() {
        return blocks.size();
    }
}
