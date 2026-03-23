package com.zombienw.onyxLib;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CustomItem {

    private final String id;
    private final ItemStack template;
    private final ItemAsset asset;

    private CustomItem(Builder builder) {
        this.id = OnyxValidation.requireValidId(builder.id);
        this.template = builder.template.clone();
        this.asset = builder.asset;
    }

    public String getId() { return id; }
    public Material getMaterial() { return template.getType(); }
    public boolean hasAsset() { return asset != null; }
    public ItemAsset getAsset() { return asset; }

    // Returns a copy of the base template (before PDC/CMD tagging).
    ItemStack cloneTemplate() { return template.clone(); }

    public static Builder builder(String id) {
        return new Builder(id);
    }

    public static class Builder {

        private final String id;
        private ItemStack template;
        private ItemAsset asset;

        public Builder(String id) {
            this.id = id;
        }

        // Fully configured item stack
        public Builder template(ItemStack stack) {
            this.template = stack.clone();
            return this;
        }

        public Builder texture(String texturePath) {
            this.asset = ItemAsset.builder(texturePath).build();
            return this;
        }

        public Builder asset(ItemAsset asset) {
            this.asset = asset;
            return this;
        }

        public CustomItem build() {
            if (template == null) {
                throw new IllegalStateException("CustomItem '" + id + "' requires a template ItemStack.");
            }
            return new CustomItem(this);
        }
    }
}