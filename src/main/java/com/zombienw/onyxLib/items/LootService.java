package com.zombienw.onyxLib.items;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LootService implements Listener {

    private final Map<Material, List<CustomDrop>> blockDrops = new HashMap<>();
    private final Map<EntityType, List<CustomDrop>> entityDrops = new HashMap<>();

    public void registerBlockDrop(Material material, ItemStack item, double chance, boolean fortune, boolean preventSilk) {
        blockDrops.computeIfAbsent(material, k -> new ArrayList<>())
                .add(new CustomDrop(item, chance, fortune, preventSilk));
    }

    public void registerEntityDrop(EntityType type, ItemStack item, double chance, boolean looting) {
        entityDrops.computeIfAbsent(type, k -> new ArrayList<>())
                .add(new CustomDrop(item, chance, looting, false));
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();

        List<CustomDrop> drops = blockDrops.get(block.getType());
        if (drops == null) return;

        for (CustomDrop drop : drops) {
            // check silk touch and shears
            if (drop.preventSilk()) {
                boolean hasSilk = tool.containsEnchantment(Enchantment.SILK_TOUCH);
                boolean isShearsOnLeaves = tool.getType() == Material.SHEARS && Tag.LEAVES.isTagged(block.getType());

                if (hasSilk || isShearsOnLeaves) continue;
            }

            // apply fortune
            double finalChance = drop.chance();
            if (drop.lootingFortuneApplies()) {
                int level = tool.getEnchantmentLevel(Enchantment.FORTUNE);
                if (level > 0) finalChance *= (level + 1);
            }

            if (Math.random() < finalChance) {
                block.getWorld().dropItemNaturally(block.getLocation(), drop.item().clone());
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onLeafDecay(LeavesDecayEvent event) {
        Block block = event.getBlock();
        List<CustomDrop> drops = blockDrops.get(block.getType());
        if (drops == null) return;

        for (CustomDrop drop : drops) {
            if (Math.random() < drop.chance()) {
                block.getWorld().dropItemNaturally(block.getLocation(), drop.item().clone());
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        List<CustomDrop> drops = entityDrops.get(event.getEntityType());
        if (drops == null) return;

        Player killer = event.getEntity().getKiller();
        ItemStack tool = (killer != null) ? killer.getInventory().getItemInMainHand() : null;

        for (CustomDrop drop : drops) {
            double finalChance = drop.chance();

            if (drop.lootingFortuneApplies() && tool != null) {
                int level = tool.getEnchantmentLevel(Enchantment.LOOTING);
                if (level > 0) finalChance *= (level + 1);
            }

            if (Math.random() < finalChance) {
                event.getDrops().add(drop.item().clone());
            }
        }
    }

    private record CustomDrop(
            ItemStack item,
            double chance,
            boolean lootingFortuneApplies,
            boolean preventSilk
    ) {}
}