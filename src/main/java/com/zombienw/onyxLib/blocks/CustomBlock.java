package com.zombienw.onyxLib.blocks;

import com.zombienw.onyxLib.core.OnyxValidation;
import com.zombienw.onyxLib.items.RegisteredItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CustomBlock {

    private final String id;
    private final Material baseMaterial;
    private final RegisteredItem registeredItem;
    private final BlockType blockType;

    private CustomBlock(Builder builder) {
        this.id = OnyxValidation.requireValidId(builder.id);
        this.baseMaterial = builder.baseMaterial;
        this.registeredItem = builder.registeredItem;
        this.blockType = builder.blockType;
    }

    public String getId() { return id; }
    public Material getBaseMaterial() { return baseMaterial; }
    public RegisteredItem getRegisteredItem() { return registeredItem; }
    public BlockType getBlockType() { return blockType; }

    public static Builder builder(String id) {
        return new Builder(id);
    }

    public static class Builder {

        private final String id;
        private Material baseMaterial = Material.BARRIER;
        private RegisteredItem registeredItem;
        private BlockType blockType = BlockType.ARMOR_STAND;

        public Builder(String id) {
            this.id = id;
        }

        // Internal hitbox block
        public Builder baseMaterial(Material material) {
            this.baseMaterial = material;
            return this;
        }

        // The custom item stack that's in frame or on the stand and placing and dropping
        public Builder item(RegisteredItem item) {
            this.registeredItem = item;
            return this;
        }

        // Which OnyxLib block mechanism to use
        public Builder type(BlockType type) {
            this.blockType = type;
            return this;
        }

        public CustomBlock build() {
            if (registeredItem  == null) {
                throw new IllegalStateException("CustomBlock '" + id + "' requires a .item().");
            }
            return new CustomBlock(this);
        }
    }
}
