package com.zombienw.onyxLib.blocks;

import com.zombienw.onyxLib.items.ItemService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Location;
import org.bukkit.Nameable;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
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
        triggerFrame.remove();

        Block block = location.getBlock();
        block.setType(registered.getBlock().getBaseMaterial());

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            nameNameable(block, registered);

            ArmorStand stand = utils.spawnMarkerStand(location, registered.getFullId());
            utils.setHelmet(stand, createDisplayItem(registered));
        }, 1L);
    }

    private void placeRotatableArmorStand(RegisteredBlock registered, Location location, Player placer, ItemFrame triggerFrame) {
        CardinalDirection facing = CardinalDirection.fromPlayer(placer);

        triggerFrame.remove();

        Block block = location.getBlock();
        block.setType(registered.getBlock().getBaseMaterial());

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            nameNameable(block, registered);

            ArmorStand stand = utils.spawnMarkerStand(location, registered.getFullId(), facing);
            utils.setHelmet(stand, createDisplayItem(registered));
        }, 1L);
    }

    private void placeItemFrame(RegisteredBlock registered, Location location, ItemFrame triggerFrame) {
        triggerFrame.remove();

        Block block = location.getBlock();
        block.setType(registered.getBlock().getBaseMaterial());

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            nameNameable(block, registered);

            utils.spawnFixedFrame(location, registered.getFullId(), createDisplayItem(registered));
        }, 1L);
    }

    private void placeRotatableItemFrame(RegisteredBlock registered, Location location, Player placer, ItemFrame triggerFrame) {
        CardinalDirection facing = CardinalDirection.fromPlayer(placer);

        triggerFrame.remove();

        Block block = location.getBlock();
        block.setType(registered.getBlock().getBaseMaterial());

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            nameNameable(block, registered);

            utils.spawnFixedFrame(location, registered.getFullId(), createDisplayItem(registered), facing);
        }, 1L);
    }

    private ItemStack createDisplayItem(RegisteredBlock registered) {
        return itemService.create(registered.getBlock().getRegisteredItem().getFullId());
    }

    private void nameNameable(Block block, RegisteredBlock registered) {
        if (block.getState() instanceof Nameable nameable) {
            nameable.customName(itemService.create(registered.getBlock().getRegisteredItem().getFullId()).effectiveName());
            ((BlockState) nameable).update();
        }
    }
}
