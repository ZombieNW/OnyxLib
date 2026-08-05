package com.zombienw.onyxlib.api.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

/**
 * Represents a custom inventory GUI (chest menu).
 * Acts as the builder for itself, similarly to OnyxItem/OnyxBlock.
 * <p>
 * Instances are created via {@link com.zombienw.onyxlib.api.OnyxLib#gui(Component, int)}
 * and are NOT registered/shared like items or blocks; each instance is a standalone
 * menu definition that can be opened for one or many players.
 */
public interface OnyxGui {

    /**
     * Sets the title of the menu.
     * @param title The title to display.
     * @return This OnyxGui instance.
     */
    OnyxGui title(Component title);

    /**
     * Sets the title of the menu using a plain, non-italic string.
     * @param title The title to display.
     * @return This OnyxGui instance.
     */
    OnyxGui title(String title);

    /**
     * Places a button in the given slot.
     * @param slot The slot index (0-based, must be within the gui's size).
     * @param stack The ItemStack to display in the slot.
     * @param onClick Called whenever a player clicks this slot. May be {@code null} for a decorative/no-op button.
     * @return This OnyxGui instance.
     */
    OnyxGui item(int slot, ItemStack stack, Consumer<OnyxGuiClickEvent> onClick);

    /**
     * Places a purely decorative button (no click behavior) in the given slot.
     * @param slot The slot index (0-based, must be within the gui's size).
     * @param stack The ItemStack to display in the slot.
     * @return This OnyxGui instance.
     */
    OnyxGui item(int slot, ItemStack stack);

    /**
     * Fills every currently-empty slot with the given filler item (no click behavior).
     * Useful for borders/backgrounds, e.g. filling with gray stained glass panes.
     * @param stack The ItemStack to fill empty slots with.
     * @return This OnyxGui instance.
     */
    OnyxGui fill(ItemStack stack);

    /**
     * Removes whatever button is in the given slot, if any.
     * @param slot The slot index to clear.
     * @return This OnyxGui instance.
     */
    OnyxGui clear(int slot);

    /**
     * Controls whether clicks inside this gui are cancelled by default (preventing
     * players from picking up/moving the buttons). Defaults to {@code true}.
     * Individual clicks can still be un-cancelled from within a button's click handler
     * via {@link OnyxGuiClickEvent#setCancelled(boolean)}.
     * @param cancel Whether to cancel clicks by default.
     * @return This OnyxGui instance.
     */
    OnyxGui cancelClicksByDefault(boolean cancel);

    /**
     * Sets a callback invoked when a viewer closes this menu.
     * @param onClose Called with the player who closed the menu.
     * @return This OnyxGui instance.
     */
    OnyxGui onClose(Consumer<Player> onClose);

    /**
     * Opens this menu for the given player.
     * @param player The player to open the menu for.
     */
    void open(Player player);

    /**
     * @return The number of slots in this menu.
     */
    int getSize();

    /**
     * @return The underlying Bukkit inventory backing this menu.
     */
    Inventory getInventory();
}
