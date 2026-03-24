package com.zombienw.onyxLib.items;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LootService implements Listener {

    private final Map<Material, List<CustomDrop>> blockDrops = new HashMap<>();
    private final Map<EntityType, List<CustomDrop>> entityDrops = new HashMap<>();

    public void registerDrop(Material material, ItemStack item, double chance) {
        blockDrops.computeIfAbsent(material, k -> new ArrayList<>())
                .add(new CustomDrop(item, chance));
    }

    public void registerEntityDrop(EntityType type, ItemStack item, double chance) {
        entityDrops.computeIfAbsent(type, k -> new ArrayList<>())
                .add(new CustomDrop(item, chance));
    }

    private void handlePotentialDrop(Block block) {
        List<CustomDrop> drops = blockDrops.get(block.getType());
        if (drops == null) return;

        for (CustomDrop drop : drops) {
            if (Math.random() < drop.chance()) {
                block.getWorld().dropItemNaturally(block.getLocation(), drop.item().clone());
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        handlePotentialDrop(event.getBlock());
    }

    @EventHandler(ignoreCancelled = true)
    public void onLeafDecay(LeavesDecayEvent event) {
        handlePotentialDrop(event.getBlock());
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        List<CustomDrop> drops = entityDrops.get(event.getEntityType());
        if (drops == null) return;

        for (CustomDrop drop : drops) {
            if (Math.random() < drop.chance()) {
                // add to the existing drop list so it respects looting
                event.getDrops().add(drop.item().clone());
            }
        }
    }

    private record CustomDrop(ItemStack item, double chance) {}
}
