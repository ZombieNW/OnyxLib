package com.zombienw.onyxlib.impl.gui;

import com.zombienw.onyxlib.api.gui.OnyxGuiClickEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.InventoryHolder;

import java.util.function.Consumer;

/**
 * Handles click, drag, and close events for all open OnyxGui menus.
 * Identifies OnyxGui inventories via their InventoryHolder rather than
 * tracking open inventories manually.
 */
public class OnyxGuiListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onClick(InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (!(holder instanceof OnyxGuiImpl gui)) return;

        // Only handle clicks on the gui's own inventory. Clicks in the player's
        // own inventory (including shift-clicks originating there) are left alone.
        if (event.getClickedInventory() == null || !event.getClickedInventory().equals(event.getInventory())) {
            return;
        }

        if (gui.cancelsClicksByDefault()) {
            event.setCancelled(true);
        }

        Consumer<OnyxGuiClickEvent> handler = gui.getClickHandler(event.getSlot());
        if (handler != null) {
            handler.accept(new OnyxGuiClickEvent(gui, event));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onDrag(InventoryDragEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (!(holder instanceof OnyxGuiImpl gui)) return;

        // Prevent dragging items across the gui's slots regardless of the default,
        // since partial drags into a menu are almost never desired.
        boolean touchesGui = event.getRawSlots().stream().anyMatch(slot -> slot < gui.getSize());
        if (touchesGui) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onClose(InventoryCloseEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (!(holder instanceof OnyxGuiImpl gui)) return;

        Consumer<Player> onClose = gui.getOnCloseHandler();
        if (onClose != null && event.getPlayer() instanceof Player player) {
            onClose.accept(player);
        }
    }
}
