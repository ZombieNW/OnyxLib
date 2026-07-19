package com.zombienw.onyxlib.impl.block;

import com.zombienw.onyxlib.OnyxPlugin;
import com.zombienw.onyxlib.api.OnyxElement;
import com.zombienw.onyxlib.api.block.OnyxBlock;
import com.zombienw.onyxlib.api.event.OnyxBlockBreakEvent;
import com.zombienw.onyxlib.api.event.OnyxBlockPlaceEvent;
import com.zombienw.onyxlib.impl.registry.NamespaceRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.BoundingBox;

/**
 * Event listener to manage the lifecycle of custom blocks.
 */
public class OnyxBlockListener implements Listener {

    private final OnyxPlugin plugin;

    private static final int LIGHT_RADIUS = 16;

    public OnyxBlockListener(OnyxPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Handles when an OnyxBlock item is placed and places the physical OnyxBlock.
     * @param event BlockPlaceEvent
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        if (!item.hasItemMeta()) return;

        // Find the "onyx_id" key
        ItemMeta meta = item.getItemMeta();
        NamespacedKey identityKey = meta.getPersistentDataContainer().getKeys().stream()
                .filter(k -> k.getKey().equals("onyx_id"))
                .findFirst()
                .orElse(null);
        if (identityKey == null) return;

        // Find block in registry from key
        String elementId = meta.getPersistentDataContainer().get(identityKey, PersistentDataType.STRING);
        if (elementId == null) return;
        NamespacedKey registryKey = new NamespacedKey(identityKey.getNamespace(), elementId);
        if (!(NamespaceRegistry.getElement(registryKey) instanceof OnyxBlock onyxBlock)) return;

        // Place the OnyxBlock
        Block block = event.getBlockPlaced();
        Location loc = block.getLocation();

        float playerYaw = event.getPlayer().getLocation().getYaw();
        loc.setYaw(Math.round(playerYaw / 90.0f) * 90.0f);
        loc.setPitch(0.0f);

        onyxBlock.place(loc);
        updateNearbyDisplays(loc);

        // Dispatch event
        plugin.getServer().getPluginManager().callEvent(new OnyxBlockPlaceEvent(block, event.getPlayer(), onyxBlock));
    }

    /**
     * Checks if broken blocks are OnyxBlocks and breaks them.
     * @param event BlockBreakEvent
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Location center = block.getLocation().add(0.5, 0.5, 0.5);

        // find first ItemDisplay with "onyx_id" key
        ItemDisplay targetDisplay = null;
        NamespacedKey identityKey = null;

        for (Entity entity : block.getWorld().getNearbyEntities(center, 0.1, 0.1, 0.1)) {
            if (entity instanceof ItemDisplay display) {
                identityKey = display.getPersistentDataContainer().getKeys().stream()
                        .filter(k -> k.getKey().equals("onyx_id"))
                        .findFirst()
                        .orElse(null);

                if (identityKey != null) {
                    targetDisplay = display;
                    break;
                }
            }
        }

        if (targetDisplay == null) return;

        // get element id using key
        String elementId = targetDisplay.getPersistentDataContainer().get(identityKey, PersistentDataType.STRING);
        if (elementId == null) return;

        // find block in registry from key
        NamespacedKey registryKey = new NamespacedKey(identityKey.getNamespace(), elementId);
        if (!(NamespaceRegistry.getElement(registryKey) instanceof OnyxBlock onyxBlock)) return;

        // dispatch event
        plugin.getServer().getPluginManager().callEvent(new OnyxBlockBreakEvent(block, event.getPlayer(), onyxBlock));

        // remove entity
        targetDisplay.remove();

        // drop item
        updateNearbyDisplays(center); // update lights
        event.setDropItems(false);
        block.getWorld().dropItemNaturally(block.getLocation(), onyxBlock.create(1));
    }

    private void updateNearbyDisplays(Location origin) {
        if (origin.getWorld() == null) return;

        origin.getWorld().getNearbyEntities(
                origin, LIGHT_RADIUS, LIGHT_RADIUS, LIGHT_RADIUS,
                e -> e instanceof ItemDisplay display && hasOnyxId(display)
        ).forEach(e -> updateDisplayLight((ItemDisplay) e));
    }

    private boolean hasOnyxId(ItemDisplay display) {
        return display.getPersistentDataContainer().getKeys().stream()
                .anyMatch(key -> key.getKey().equals("onyx_id"));
    }

    private void updateDisplayLight(ItemDisplay display) {
        Location loc = display.getLocation();
        Block block = loc.getBlock();

        int blockLight = block.getLightFromBlocks();
        int skyLight = block.getLightFromSky();
        int combined = Math.max(blockLight, skyLight);

        display.setBrightness(new Display.Brightness(combined, combined));
    }
}
