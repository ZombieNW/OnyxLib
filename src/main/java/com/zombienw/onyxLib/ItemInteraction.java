package com.zombienw.onyxLib;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

/**
 * @param clickedBlock null when clicking air
 */
public record ItemInteraction(Player player, ItemStack item, Action action, Block clickedBlock, EquipmentSlot hand) {

    public boolean isRightClick() {
        return action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK;
    }

    public boolean isLeftClick() {
        return action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK;
    }

    public boolean isClickingBlock() {
        return clickedBlock != null;
    }
}
