package com.zombienw.onyxLib;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class ItemInteractionListener implements Listener {

    private final ItemService itemService;

    public ItemInteractionListener(ItemService itemService) {
        this.itemService = itemService;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        // Ignore off-hand to avoid double-firing
        if (event.getHand() == EquipmentSlot.OFF_HAND) return;

        Action action = event.getAction();

        // Filter to only actual clicks (not PHYSICAL which fires on pressure plates)
        if (action == Action.PHYSICAL) return;

        ItemStack item = event.getItem();
        if (item == null || !itemService.isCustomItem(item)) return;

        ItemInteraction interaction = new ItemInteraction(
                event.getPlayer(),
                item,
                action,
                event.getClickedBlock(),
                event.getHand()
        );

        itemService.dispatch(item, interaction);
    }
}