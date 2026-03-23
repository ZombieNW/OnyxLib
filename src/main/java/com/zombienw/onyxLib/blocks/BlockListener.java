package com.zombienw.onyxLib.blocks;

import com.zombienw.onyxLib.items.ItemService;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;

import static com.zombienw.onyxLib.blocks.BlockEntityUtils.findNearestPlayer;

public class BlockListener implements Listener {

    private final BlockService blockService;
    private final ItemService itemService;

    public BlockListener(BlockService blockService, ItemService itemService) {
        this.blockService = blockService;
        this.itemService = itemService;
    }

    /// Place
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onItemFrameSpawn(EntitySpawnEvent event) {
        if (event.getEntityType() != EntityType.ITEM_FRAME) return;

        ItemFrame frame = (ItemFrame) event.getEntity();

        Player placer = findNearestPlayer(frame, 5);
        if (placer == null) return;

        ItemStack held = placer.getInventory().getItemInMainHand();
        if (held.getType() == Material.AIR || !held.hasItemMeta()) return;

        // Held item must be a registered OnyxLib item whose id matches a registered block
        itemService.getId(held).flatMap(blockService::get).ifPresent(registered ->
                blockService.handlePlace(
                        registered.getFullId(),
                        frame.getLocation(),
                        placer,
                        frame
                )
        );
    }

    /// Break
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();

        blockService.utils().findMarkerAt(block.getLocation()).ifPresent(marker -> {
            event.setDropItems(false);
            blockService.handleBreak(marker, block.getLocation());
        });
    }
}
