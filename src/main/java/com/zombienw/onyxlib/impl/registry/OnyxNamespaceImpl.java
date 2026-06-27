package com.zombienw.onyxlib.impl.registry;

import com.zombienw.onyxlib.api.OnyxElement;
import com.zombienw.onyxlib.api.OnyxNamespace;
import com.zombienw.onyxlib.api.block.OnyxBlock;
import com.zombienw.onyxlib.api.item.OnyxItem;
import com.zombienw.onyxlib.impl.block.OnyxBlockImpl;
import com.zombienw.onyxlib.impl.item.OnyxItemImpl;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implements OnyxNamespaces
 */
public class OnyxNamespaceImpl implements OnyxNamespace {

    private final Plugin plugin;
    private final NamespacedKey namespaceKey;
    private final NamespacedKey onyxKey; // unified key for all elements
    private volatile boolean isLocked = false;

    private final Map<String, OnyxElement> allElements = new ConcurrentHashMap<>();
    private final Map<String, OnyxItemImpl> registeredItems = new ConcurrentHashMap<>();
    private final Map<String, OnyxBlockImpl> registeredBlocks = new ConcurrentHashMap<>();

    public OnyxNamespaceImpl(Plugin plugin, String namespaceId) {
        this.plugin = plugin;
        this.namespaceKey = new NamespacedKey(plugin, namespaceId);
        this.onyxKey = new NamespacedKey(plugin, "onyx_id");
    }

    public Plugin getPlugin() { return this.plugin; }

    @Override
    public NamespacedKey getKey() {
        return this.namespaceKey;
    }

    @Override
    public OnyxItem item(String id) {
        if (this.isLocked) throwLockedException(id);

        if (this.allElements.containsKey(id)) {
            throw new IllegalArgumentException("Conflict: Cannot register item '" + id +
                    "' because this ID already exists in namespace '" + plugin.getName() + "'.");
        }

        NamespacedKey itemKey = new NamespacedKey(this.plugin, id);
        OnyxItemImpl item = new OnyxItemImpl(id, itemKey, this.onyxKey);

        OnyxItemImpl existing = registeredItems.putIfAbsent(id, item);
        if (existing != null) {
            return existing;
        }

        allElements.put(id, item);
        return item;
    }

    @Override
    public OnyxBlock block(String id) {
        if (this.isLocked) throwLockedException(id);

        if (this.allElements.containsKey(id)) {
            throw new IllegalArgumentException("Conflict: Cannot register block '" + id +
                    "' because this ID already exists in namespace '" + plugin.getName() + "'.");
        }

        NamespacedKey blockKey = new NamespacedKey(this.plugin, id);
        OnyxBlockImpl block = new OnyxBlockImpl(id, blockKey, this.onyxKey);

        OnyxBlockImpl existing = registeredBlocks.putIfAbsent(id, block);
        if (existing != null) {
            return existing;
        }

        allElements.put(id, block);
        return block;
    }

    @Override
    public OnyxItem matchItem(ItemStack stack) {
        if (stack == null || !stack.hasItemMeta()) return null;

        ItemMeta meta = stack.getItemMeta();
        if (meta == null) return null;

        String onyxId = meta.getPersistentDataContainer().get(onyxKey, PersistentDataType.STRING);
        return onyxId != null ? registeredItems.get(onyxId) : null;
    }

    @Override
    public OnyxBlock matchBlock(Block block) {
        if (block == null) return null;

        Location loc = block.getLocation().add(0.5, 0.5, 0.5);
        if (loc.getWorld() == null) return null;

        return loc.getWorld().getNearbyEntities(loc, 0.25, 0.25, 0.25).stream()
                .filter(entity -> entity instanceof ItemDisplay)
                .map(entity -> (ItemDisplay) entity)
                .filter(display -> display.getPersistentDataContainer().has(onyxKey, PersistentDataType.STRING))
                .map(display -> {
                    String onyxId = display.getPersistentDataContainer().get(onyxKey, PersistentDataType.STRING);
                    return onyxId != null ? registeredBlocks.get(onyxId) : null;
                })
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    private void throwLockedException(String id) {
        throw new IllegalStateException(
                "Cannot register or modify element '" + id + "'. The namespace for " +
                        plugin.getName() + " is currently locked."
        );
    }

    /**
     * Locks the namespace to prevent further items from being registered
     */
    public void lock() {
        if (this.isLocked) return;
        this.isLocked = true;

        // Textures are now validated by the pack generator
        // Base materials are now validated at ItemStack creation
    }

    public Collection<OnyxItemImpl> getItems() { return this.registeredItems.values(); }
    public OnyxItemImpl getItem(String id) { return this.registeredItems.get(id); }

    public Collection<OnyxBlockImpl> getBlocks() { return this.registeredBlocks.values(); }
    public OnyxBlockImpl getBlock(String id) { return this.registeredBlocks.get(id); }

    public OnyxElement getElement(String id) { return this.allElements.get(id); }
    public Collection<OnyxElement> getElements() { return this.allElements.values(); }
}
