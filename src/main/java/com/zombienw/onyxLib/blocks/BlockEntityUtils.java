package com.zombienw.onyxLib.blocks;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;

public class BlockEntityUtils {

    private final NamespacedKey blockIdKey;
    private final NamespacedKey blockMarkerKey;

    public BlockEntityUtils(JavaPlugin plugin) {
        this.blockIdKey = new NamespacedKey(plugin, "block_id");
        this.blockMarkerKey = new NamespacedKey(plugin, "block_marker");
    }

    // Spawns a funny armor stand at the center of the block
    // invisible, persistent, marker, no gravity, invulnerable, silent
    public ArmorStand spawnMarkerStand(Location location, String blockId) {
        Location centered = blockCenter(location);
        ArmorStand stand = location.getWorld().spawn(centered, ArmorStand.class, as -> {
            as.setGravity(false);
            as.setInvulnerable(true);
            as.setInvisible(true);
            as.setSilent(true);
            as.setMarker(true);
            as.setPersistent(true);
            as.setSmall(true);
            tagEntity(as, blockId);
        });
        return stand;
    }

    // above with rotation
    public ArmorStand spawnMarkerStand(Location location, String blockId, CardinalDirection facing) {
        ArmorStand stand = spawnMarkerStand(location, blockId);
        stand.setRotation(facing.toArmorStandYaw(), 0f);
        return stand;
    }

    public void setHelmet(ArmorStand stand, ItemStack item) {
        EntityEquipment eq = stand.getEquipment();
        if (eq != null) eq.setHelmet(item);
    }

    // spawns a funny item frame at center of block
    // fixed, invisible, persistent, facing up
    public ItemFrame spawnFixedFrame(Location location, String blockId, ItemStack displayItem) {
        Location centered = blockCenter(location);
        ItemFrame frame = location.getWorld().spawn(centered, ItemFrame.class, f -> {
            f.setInvulnerable(true);
            f.setVisible(false);
            f.setFixed(true);
            f.setPersistent(true);
            f.setItem(displayItem);
            tagEntity(f, blockId);
        });
        return frame;
    }

    public void tagEntity(Entity entity, String blockId) {
        var pdc = entity.getPersistentDataContainer();
        pdc.set(blockIdKey, PersistentDataType.STRING, blockId);
        pdc.set(blockMarkerKey, PersistentDataType.BOOLEAN, true);
    }

    public boolean isOnyxBlock(Entity entity) {
        return entity.getPersistentDataContainer()
                .has(blockMarkerKey, PersistentDataType.BOOLEAN);
    }

    public Optional<String> getBlockId(Entity entity) {
        return Optional.ofNullable(
                entity.getPersistentDataContainer()
                        .get(blockIdKey, PersistentDataType.STRING)
        );
    }

    public static Location blockCenter(Location loc) {
        return new Location(
                loc.getWorld(),
                loc.getBlockX() + 0.5,
                loc.getBlockY(),
                loc.getBlockZ() + 0.5
        );
    }

    public Optional<Entity> findMarkerAt(Location location) {
        int bx = location.getBlockX();
        int by = location.getBlockY();
        int bz = location.getBlockZ();

        Collection<Entity> nearby = location.getWorld()
                .getNearbyEntities(location, 1, 1, 1);

        return nearby.stream()
                .filter(this::isOnyxBlock)
                .filter(e -> e.getLocation().getBlockX() == bx
                        && e.getLocation().getBlockY() == by
                        && e.getLocation().getBlockZ() == bz)
                .findFirst();
    }

    public static Player findNearestPlayer(Entity near, double radius) {
        return near.getLocation().getWorld()
                .getNearbyEntities(near.getLocation(), radius, radius, radius)
                .stream()
                .filter(e -> e instanceof Player)
                .map(e -> (Player) e)
                .min(Comparator.comparingDouble(a -> a.getLocation().distanceSquared(near.getLocation())))
                .orElse(null);
    }
}
