package com.zombienw.onyxLib.core;

import com.zombienw.onyxLib.OnyxLib;
import com.zombienw.onyxLib.blocks.BlockEntityUtils;
import com.zombienw.onyxLib.blocks.BlockService;
import com.zombienw.onyxLib.blocks.CustomBlock;
import com.zombienw.onyxLib.blocks.RegisteredBlock;
import com.zombienw.onyxLib.core.OnyxValidation;
import com.zombienw.onyxLib.items.CustomItem;
import com.zombienw.onyxLib.items.ItemService;
import com.zombienw.onyxLib.items.RegisteredItem;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

public class OnyxNamespace {

    private final String namespace;
    private final ItemNamespace itemNamespace;
    private final BlockNamespace blockNamespace;

    public OnyxNamespace(String namespace, OnyxLib lib, JavaPlugin owningPlugin) {
        this.namespace = OnyxValidation.requireValidNamespace(namespace.toLowerCase());
        this.itemNamespace = new ItemNamespace(this.namespace, lib.items(), owningPlugin);
        this.blockNamespace = new BlockNamespace(this.namespace, lib.blocks(), owningPlugin);
    }

    public String getNamespace() { return namespace; }
    public ItemNamespace items() { return itemNamespace; }
    public BlockNamespace blocks() { return blockNamespace; }

    public BlockEntityUtils blockUtils() {
        return blockNamespace.blockService.utils();
    }

    /// Item Namespace

    public static class ItemNamespace {

        private final String namespace;
        private final ItemService itemService;
        private final JavaPlugin owningPlugin;

        ItemNamespace(String namespace, ItemService itemService, JavaPlugin owningPlugin) {
            this.namespace = namespace;
            this.itemService = itemService;
            this.owningPlugin = owningPlugin;
        }

        public RegisteredItem register(CustomItem item) {
            return itemService.register(namespace, item, owningPlugin);
        }

        public ItemStack create(String localId) {
            return itemService.create(namespace + ":" + localId);
        }

        public ItemStack create(String localId, int amount) {
            return itemService.create(namespace + ":" + localId, amount);
        }

        public Optional<RegisteredItem> get(String localId) {
            return itemService.get(namespace + ":" + localId);
        }
    }

    /// Block Namespace

    public static class BlockNamespace {

        private final String namespace;
        private final BlockService blockService;
        private final JavaPlugin owningPlugin;

        BlockNamespace(String namespace, BlockService blockService, JavaPlugin owningPlugin) {
            this.namespace = namespace;
            this.blockService = blockService;
            this.owningPlugin = owningPlugin;
        }

        public RegisteredBlock register(CustomBlock block) {
            return blockService.register(namespace, block, owningPlugin);
        }

        public Optional<RegisteredBlock> get(String localId) {
            return blockService.get(namespace + ":" + localId);
        }
    }
}