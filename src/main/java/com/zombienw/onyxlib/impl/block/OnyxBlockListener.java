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

public class OnyxBlockListener implements Listener {

    private final OnyxPlugin plugin;

    public OnyxBlockListener(OnyxPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();
        if (!item.hasItemMeta()) return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;

        // get element key
        NamespacedKey identityKey = meta.getPersistentDataContainer().getKeys().stream()
                .filter(k -> k.getKey().equals("onyx_id"))
                .findFirst()
                .orElse(null);
        if (identityKey == null) return;

        // find element
        String elementId = meta.getPersistentDataContainer().get(identityKey, PersistentDataType.STRING);
        if (elementId == null) return;

        // find block
        NamespacedKey registryKey = new NamespacedKey(identityKey.getNamespace(), elementId);
        if (!(NamespaceRegistry.getElement(registryKey) instanceof OnyxBlock onyxBlock)) return;

        // setup block
        Block block = event.getBlockPlaced();
        Location loc = block.getLocation();

        // get player yaw
        loc.setYaw(Math.round(event.getPlayer().getLocation().getYaw() / 90.0f) * 90.0f);
        loc.setPitch(0.0f);

        // place block
        onyxBlock.place(loc);

        // fire onyx event
        plugin.getServer().getPluginManager().callEvent(new OnyxBlockPlaceEvent(block, event.getPlayer(), onyxBlock));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Location center = block.getLocation().add(0.5, 0.5, 0.5);

        // find item display
        ItemDisplay display = block.getWorld().getNearbyEntities(center, 0.1, 0.1, 0.1).stream()
                .filter(ItemDisplay.class::isInstance)
                .map(ItemDisplay.class::cast)
                .filter(e -> e.getPersistentDataContainer().getKeys().stream().anyMatch(k -> k.getKey().equals("onyx_id")))
                .findFirst()
                .orElse(null);

        if (display == null) return;

        // get key
        NamespacedKey identityKey = display.getPersistentDataContainer().getKeys().stream()
                .filter(k -> k.getKey().equals("onyx_id"))
                .findFirst()
                .orElse(null);
        if (identityKey == null) return;

        String elementId = display.getPersistentDataContainer().get(identityKey, PersistentDataType.STRING);
        if (elementId == null) return;

        // get block
        NamespacedKey registryKey = new NamespacedKey(identityKey.getNamespace(), elementId);
        if (!(NamespaceRegistry.getElement(registryKey) instanceof OnyxBlock onyxBlock)) return;

        // dispatch event
        plugin.getServer().getPluginManager().callEvent(new OnyxBlockBreakEvent(block, event.getPlayer(), onyxBlock));

        display.remove(); // remove entity

        // override drops
        event.setDropItems(false);
        block.getWorld().dropItemNaturally(block.getLocation(), onyxBlock.create(1));
    }
}
