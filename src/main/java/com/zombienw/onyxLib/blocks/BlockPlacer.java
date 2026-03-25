package com.zombienw.onyxLib.blocks;

import com.zombienw.onyxLib.items.ItemService;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Nameable;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockPlacer {

    private final BlockEntityUtils utils;
    private final ItemService itemService;
    private final JavaPlugin plugin;

    BlockPlacer(BlockEntityUtils utils, ItemService itemService, JavaPlugin plugin) {
        this.utils = utils;
        this.itemService = itemService;
        this.plugin = plugin;
    }

    void place(RegisteredBlock registered, Location location, Player placer, ItemFrame triggerFrame) {
        switch (registered.getBlock().getBlockType()) {
            case ARMOR_STAND -> placeArmorStand(registered, location, triggerFrame);
            case ARMOR_STAND_ROTATABLE -> placeRotatableArmorStand(registered, location, placer, triggerFrame);
            case ITEM_FRAME -> placeItemFrame(registered, location, triggerFrame);
            case ITEM_FRAME_ROTATABLE -> placeRotatableItemFrame(registered, location, placer, triggerFrame);
        }
    }

    private void placeArmorStand(RegisteredBlock registered, Location location, ItemFrame triggerFrame) {
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            triggerFrame.remove();

            Block block = location.getBlock();
            block.setType(registered.getBlock().getBaseMaterial());

            // Rename block if applicable (Chest, Barrel, Furnace, etc)
            BlockState state = block.getState();
            if (state instanceof Nameable nameable) {
                Component itemName = itemService.create(registered.getBlock().getRegisteredItem().getItem().getId()).displayName();
                nameable.customName(itemName);
                state.update();
            }

            ArmorStand stand = utils.spawnMarkerStand(location, registered.getFullId());
            utils.setHelmet(stand, createDisplayItem(registered));
        }, 1L);
    }

    private void placeRotatableArmorStand(RegisteredBlock registered, Location location, Player placer, ItemFrame triggerFrame) {
        CardinalDirection facing = CardinalDirection.fromPlayer(placer);

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            triggerFrame.remove();
            location.getBlock().setType(registered.getBlock().getBaseMaterial());

            ArmorStand stand = utils.spawnMarkerStand(location, registered.getFullId(), facing);
            utils.setHelmet(stand, createDisplayItem(registered));
        }, 1L);
    }

    private void placeItemFrame(RegisteredBlock registered, Location location, ItemFrame triggerFrame) {
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            triggerFrame.remove();
            location.getBlock().setType(registered.getBlock().getBaseMaterial());

            utils.spawnFixedFrame(location, registered.getFullId(), createDisplayItem(registered));
        }, 1L);
    }

    private void placeRotatableItemFrame(RegisteredBlock registered, Location location, Player placer, ItemFrame triggerFrame) {
        CardinalDirection facing = CardinalDirection.fromPlayer(placer);

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            triggerFrame.remove();
            location.getBlock().setType(registered.getBlock().getBaseMaterial());
            utils.spawnFixedFrame(location, registered.getFullId(), createDisplayItem(registered), facing);
        }, 1L);
    }

    private ItemStack createDisplayItem(RegisteredBlock registered) {
        return itemService.create(registered.getBlock().getRegisteredItem().getFullId());
    }
}
