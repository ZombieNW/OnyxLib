package com.zombienw.onyxLib.items;

import com.zombienw.onyxLib.blocks.BlockModel;

public class ItemAsset {

    public static final String DEFAULT_MODEL_PARENT = "item/generated";

    private final String texturePath;
    private final String modelParent;
    private final boolean customModel;
    private final BlockModel blockModel;

    private ItemAsset(Builder builder) {
        this.texturePath = builder.texturePath;
        this.modelParent = builder.modelParent;
        this.customModel = builder.customModel;
        this.blockModel = builder.blockModel;
    }

    public String getTexturePath() { return texturePath; }
    public String getModelParent() { return modelParent; }
    public boolean isCustomModel() { return customModel; }
    public BlockModel getBlockModel() { return blockModel; }

    // Basic Items
    public static Builder builder(String texturePath) {
        return new Builder().texturePath(texturePath);
    }

    // Block Models
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String texturePath;
        private String modelParent = DEFAULT_MODEL_PARENT;
        private boolean customModel = false;
        private BlockModel blockModel = null;

        public Builder texturePath(String texturePath) {
            this.texturePath = texturePath;
            return this;
        }

        public Builder modelParent(String modelParent) {
            this.modelParent = modelParent;
            return this;
        }

        public Builder customModel() {
            this.customModel = true;
            return this;
        }

        public Builder blockModel(BlockModel blockModel) {
            this.blockModel = blockModel;
            return this;
        }

        public ItemAsset build() {
            return new ItemAsset(this);
        }
    }
}
