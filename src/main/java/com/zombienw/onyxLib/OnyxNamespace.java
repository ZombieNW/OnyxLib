package com.zombienw.onyxLib;

import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class OnyxNamespace {
    private final String namespace;
    private final ItemNamespace itemNamespace;

    public OnyxNamespace(String namespace, OnyxLib lib) {
        this.namespace = OnyxValidation.requireValidNamespace(namespace.toLowerCase());
        this.itemNamespace = new ItemNamespace(this.namespace, lib.items());
    }

    public String getNamespace() { return namespace; }

    public ItemNamespace items() { return itemNamespace; }

    public static class ItemNamespace {

        private final String namespace;
        private final ItemService itemService;

        ItemNamespace(String namespace, ItemService itemService) {
            this.namespace = namespace;
            this.itemService = itemService;
        }

        public RegisteredItem register(CustomItem item) {
            return itemService.register(namespace, item);
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
