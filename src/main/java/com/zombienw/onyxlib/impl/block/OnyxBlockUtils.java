package com.zombienw.onyxlib.impl.block;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;

public class OnyxBlockUtils {

    private static final int LIGHT_RADIUS = 16;

    /**
     * Update all nearby OnyxBlock ItemDisplay entity light values.
     * @param origin The center block
     */
    public static void updateNearbyDisplays(Location origin) {
        if (origin.getWorld() == null) return;

        origin.getWorld().getNearbyEntities(
                origin, LIGHT_RADIUS, LIGHT_RADIUS, LIGHT_RADIUS,
                e -> e instanceof ItemDisplay display && hasOnyxId(display)
        ).forEach(e -> updateDisplayLight((ItemDisplay) e));
    }

    /**
     * Sets the display brightness of an entity based on its environment
     * @param display The ItemDisplay entity to re-light
     */
    public static void updateDisplayLight(ItemDisplay display) {
        Display.Brightness dynamicBrightness = getMaximumNearbyLight(display.getLocation());
        display.setBrightness(dynamicBrightness);
    }

    /**
     * Checks if an ItemDisplay entity has any namespaced tag ending with "onyx_id"
     * @param display The ItemDisplay to check
     * @return True if a tag is found, false otherwise
     */
    public static boolean hasOnyxId(ItemDisplay display) {
        return display.getPersistentDataContainer().getKeys().stream()
                .anyMatch(key -> key.getKey().equals("onyx_id"));
    }

    /**
     * Samples nearby neighbors to get the max appropriate lighting.
     * @param loc The center location to sample around.
     * @return A Display.Brightness object with the max block/sky lighting values
     */
    public static Display.Brightness getMaximumNearbyLight(Location loc) {
        World world = loc.getWorld();
        if (world == null) {
            return new Display.Brightness(0, 0);
        }

        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();

        Block centerBlock = world.getBlockAt(x, y, z);
        int maxBlockLight = centerBlock.getLightFromBlocks();
        int maxSkyLight = centerBlock.getLightFromSky();

        int[][] directions = {
                {0, 1, 0},  {0, -1, 0},
                {1, 0, 0},  {-1, 0, 0},
                {0, 0, 1},  {0, 0, -1}
        };

        for (int[] offset : directions) {
            Block neighbor = world.getBlockAt(x + offset[0], y + offset[1], z + offset[2]);
            maxBlockLight = Math.max(maxBlockLight, neighbor.getLightFromBlocks());
            maxSkyLight = Math.max(maxSkyLight, neighbor.getLightFromSky());
        }

        return new Display.Brightness(maxBlockLight, maxSkyLight);
    }
}
