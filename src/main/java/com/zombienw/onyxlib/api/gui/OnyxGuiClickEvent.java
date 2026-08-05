package com.zombienw.onyxlib.api.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * A lightweight wrapper around an {@link InventoryClickEvent} passed to a button's
 * click handler. Exposes the details plugins usually need without requiring them
 * to work with raw Bukkit inventory events.
 */
public class OnyxGuiClickEvent {

    private final OnyxGui gui;
    private final InventoryClickEvent bukkitEvent;

    public OnyxGuiClickEvent(OnyxGui gui, InventoryClickEvent bukkitEvent) {
        this.gui = gui;
        this.bukkitEvent = bukkitEvent;
    }

    /**
     * @return The OnyxGui menu that was clicked.
     */
    public OnyxGui getGui() {
        return this.gui;
    }

    /**
     * @return The player who clicked.
     */
    public Player getPlayer() {
        return (Player) this.bukkitEvent.getWhoClicked();
    }

    /**
     * @return The slot index (relative to the gui's inventory) that was clicked.
     */
    public int getSlot() {
        return this.bukkitEvent.getSlot();
    }

    /**
     * @return The type of click performed (left, right, shift-click, etc).
     */
    public ClickType getClickType() {
        return this.bukkitEvent.getClick();
    }

    /**
     * @return The raw underlying Bukkit event, for advanced use cases.
     */
    public InventoryClickEvent getBukkitEvent() {
        return this.bukkitEvent;
    }

    /**
     * Sets whether the underlying inventory click should be cancelled.
     * OnyxGui cancels all clicks inside the gui by default, so this is mainly
     * useful for allowing an action (e.g. letting a player take a specific item).
     * @param cancelled Whether to cancel the click.
     */
    public void setCancelled(boolean cancelled) {
        this.bukkitEvent.setCancelled(cancelled);
    }
}
