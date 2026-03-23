package com.zombienw.onyxLib.core;

import com.zombienw.onyxLib.OnyxLib;
import com.zombienw.onyxLib.items.CustomItem;
import com.zombienw.onyxLib.items.ItemService;
import com.zombienw.onyxLib.items.RegisteredItem;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Optional;

public class OnyxNamespace {
    private final String namespace;
    private final ItemNamespace itemNamespace;

    public OnyxNamespace(String namespace, OnyxLib lib, JavaPlugin owningPlugin) {
        this.namespace = OnyxValidation.requireValidNamespace(namespace.toLowerCase());
        this.itemNamespace = new ItemNamespace(this.namespace, lib.items(), owningPlugin);
    }

    public String getNamespace() { return namespace; }

    public ItemNamespace items() { return itemNamespace; }

    /// ItemNamespace Class
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
}
