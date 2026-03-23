package com.zombienw.onyxLib.blocks;

import com.zombienw.onyxLib.core.OnyxValidation;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CustomBlock {

    private final String id;
    private final Material baseMaterial;
    private final ItemStack displayItem;
    private final BlockType blockType;

    private CustomBlock(Builder builder) {
        this.id = OnyxValidation.requireValidId(builder.id);
        this.baseMaterial = builder.baseMaterial;
        this.displayItem = builder.displayItem.clone();
        this.blockType = builder.blockType;
    }

    public String getId() { return id; }
    public Material getBaseMaterial() { return baseMaterial; }
    public ItemStack getDisplayItem() { return displayItem.clone(); }
    public BlockType getBlockType() { return blockType; }

    public static class Builder {

        private final String id;
        private Material baseMaterial = Material.BARRIER;
        private ItemStack displayItem;
        private BlockType blockType = BlockType.ARMOR_STAND;

        public Builder(String id) {
            this.id = id;
        }

        // Internal hitbox block
        public Builder baseMaterial(Material material) {
            this.baseMaterial = material;
            return this;
        }

        // The item stack that's in frame or on the stand
        public Builder displayItem(ItemStack item) {
            this.displayItem = item.clone();
            return this;
        }

        // Which OnyxLib block mechanism to use
        public Builder type(BlockType type) {
            this.blockType = type;
            return this;
        }

        public CustomBlock build() {
            if (displayItem == null) {
                throw new IllegalStateException("CustomBlock '" + id + "' requires a displayItem.");
            }
            return new CustomBlock(this);
        }
    }
}
