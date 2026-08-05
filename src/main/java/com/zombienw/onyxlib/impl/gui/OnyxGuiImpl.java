package com.zombienw.onyxlib.impl.gui;

import com.zombienw.onyxlib.api.gui.OnyxGui;
import com.zombienw.onyxlib.api.gui.OnyxGuiClickEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Implements OnyxGui.
 * Acts as its own InventoryHolder so OnyxGuiListener can identify which
 * inventories belong to OnyxLib and route clicks to the right button handler.
 */
public class OnyxGuiImpl implements OnyxGui, InventoryHolder {

    private final int size;
    private Component title;
    private Inventory inventory;

    private final Map<Integer, Consumer<OnyxGuiClickEvent>> clickHandlers = new HashMap<>();
    private Consumer<Player> onCloseHandler;
    private boolean cancelClicksByDefault = true;

    public OnyxGuiImpl(Component title, int rows) {
        if (rows < 1 || rows > 6) {
            throw new IllegalArgumentException("OnyxGui rows must be between 1 and 6 (got " + rows + ").");
        }

        this.title = title != null ? title : Component.text("Menu");
        this.size = rows * 9;
        this.inventory = Bukkit.createInventory(this, this.size, this.title);
    }

    @Override
    public OnyxGui title(Component newTitle) {
        this.title = newTitle != null ? newTitle : Component.text("Menu");
        rebuildInventoryPreservingContents();
        return this;
    }

    @Override
    public OnyxGui title(String newTitle) {
        return title(Component.text(newTitle).decoration(TextDecoration.ITALIC, false));
    }

    @Override
    public OnyxGui item(int slot, ItemStack stack, Consumer<OnyxGuiClickEvent> onClick) {
        validateSlot(slot);

        this.inventory.setItem(slot, stack);

        if (onClick != null) {
            this.clickHandlers.put(slot, onClick);
        } else {
            this.clickHandlers.remove(slot);
        }

        return this;
    }

    @Override
    public OnyxGui item(int slot, ItemStack stack) {
        return item(slot, stack, null);
    }

    @Override
    public OnyxGui fill(ItemStack stack) {
        for (int slot = 0; slot < this.size; slot++) {
            if (this.inventory.getItem(slot) == null) {
                this.inventory.setItem(slot, stack);
            }
        }
        return this;
    }

    @Override
    public OnyxGui clear(int slot) {
        validateSlot(slot);
        this.inventory.setItem(slot, null);
        this.clickHandlers.remove(slot);
        return this;
    }

    @Override
    public OnyxGui cancelClicksByDefault(boolean cancel) {
        this.cancelClicksByDefault = cancel;
        return this;
    }

    @Override
    public OnyxGui onClose(Consumer<Player> onClose) {
        this.onCloseHandler = onClose;
        return this;
    }

    @Override
    public void open(Player player) {
        player.openInventory(this.inventory);
    }

    @Override
    public int getSize() {
        return this.size;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }

    public boolean cancelsClicksByDefault() {
        return this.cancelClicksByDefault;
    }

    @Nullable
    public Consumer<OnyxGuiClickEvent> getClickHandler(int slot) {
        return this.clickHandlers.get(slot);
    }

    @Nullable
    public Consumer<Player> getOnCloseHandler() {
        return this.onCloseHandler;
    }

    private void validateSlot(int slot) {
        if (slot < 0 || slot >= this.size) {
            throw new IndexOutOfBoundsException(
                    "Slot " + slot + " is out of bounds for an OnyxGui of size " + this.size + "."
            );
        }
    }

    // Bukkit inventories can't have their title changed in place, so title()
    // rebuilds a new inventory and copies the previous contents/viewers over.
    private void rebuildInventoryPreservingContents() {
        Inventory rebuilt = Bukkit.createInventory(this, this.size, this.title);
        rebuilt.setContents(this.inventory.getContents());
        this.inventory = rebuilt;
    }
}
