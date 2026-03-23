package com.zombienw.onyxLib.blocks;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class BlockService {

    private final Map<String, RegisteredBlock> blocks = new HashMap<>();
    private final BlockEntityUtils utils;
    private final BlockPlacer placer;

    public BlockEntityUtils utils() { return utils; }

    public BlockService(JavaPlugin plugin) {
        this.utils = new BlockEntityUtils(plugin);
        this.placer = new BlockPlacer(utils, plugin);
    }

    /// Register
    public RegisteredBlock register(String namespace, CustomBlock block, JavaPlugin owningPlugin) {
        String fullId = namespace + ":" + block.getId();

        if (blocks.containsKey(fullId)) {
            throw new IllegalArgumentException("Duplicate custom block id: " + fullId);
        }

        RegisteredBlock registered = new RegisteredBlock(namespace, block, owningPlugin);
        blocks.put(fullId, registered);
        return registered;
    }

    /// Search
    public Optional<RegisteredBlock> get(String fullId) {
        return Optional.ofNullable(blocks.get(fullId));
    }

    public Collection<RegisteredBlock> all() {
        return Collections.unmodifiableCollection(blocks.values());
    }

    /// Placing/Breaking Dispatch
    public void handlePlace(String fullId, Location location, Player placer, ItemFrame triggerFrame) {
        get(fullId).ifPresent(registered ->
                this.placer.place(registered, location, placer, triggerFrame)
        );
    }

    public void handleBreak(Entity markerEntity, Location dropLocation) {
        utils.getBlockId(markerEntity).flatMap(this::get).ifPresent(registered -> {
            markerEntity.remove();
            dropLocation.getWorld().dropItemNaturally(
                    dropLocation, registered.getBlock().getDisplayItem()
            );
        });
    }

    /// Id/Checks
    public boolean isCustomBlock(Entity entity) {
        return utils.isOnyxBlock(entity);
    }

    public boolean is(Entity entity, String fullId) {
        return utils.getBlockId(entity).map(id -> id.equals(fullId)).orElse(false);
    }

    public boolean isInNamespace(Entity entity, String namespace) {
        return utils.getBlockId(entity).map(id -> id.startsWith(namespace + ":")).orElse(false);
    }
}
