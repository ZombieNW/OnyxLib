package com.zombienw.onyxLib;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CustomItem {

    private final String id;
    private final ItemStack template;

    private CustomItem(String id, ItemStack template) {
        this.id = OnyxValidation.requireValidId(id);
        this.template = template.clone();
    }

    public String getId() {
        return id;
    }

    public Material getMaterial() {
        return template.getType();
    }

    ItemStack cloneTemplate() {
        return template.clone();
    }

    public static Builder builder(String id) {
        return new Builder(id);
    }

    public static class Builder {

        private final String id;
        private ItemStack template;

        public Builder(String id) {
            this.id = id;
        }

        // Fully configured item stack
        public Builder template(ItemStack stack) {
            this.template = stack.clone();
            return this;
        }

        public CustomItem build() {
            if (template == null) {
                throw new IllegalStateException("CustomItem '" + id + "' requires a template ItemStack.");
            }
            return new CustomItem(id, template);
        }
    }
}