package com.zombienw.onyxLib.items;

public class ItemAsset {

    public static final String DEFAULT_MODEL_PARENT = "item/generated";

    private final String texturePath;
    private final String modelParent;
    private final boolean customModel;

    private ItemAsset(Builder builder) {
        this.texturePath = builder.texturePath;
        this.modelParent = builder.modelParent;
        this.customModel = builder.customModel;
    }

    public String getTexturePath() { return texturePath; }
    public String getModelParent() { return modelParent; }

    public boolean isCustomModel() { return customModel; }

    // -------------------------------------------------------------------------

    public static Builder builder(String texturePath) {
        return new Builder(texturePath);
    }

    public static class Builder {

        private final String texturePath;
        private String modelParent = DEFAULT_MODEL_PARENT;
        private boolean customModel = false;

        public Builder(String texturePath) {
            this.texturePath = texturePath;
        }

        public Builder modelParent(String modelParent) {
            this.modelParent = modelParent;
            return this;
        }

        // tells builder we have a json in mind
        public Builder customModel() {
            this.customModel = true;
            return this;
        }

        public ItemAsset build() {
            return new ItemAsset(this);
        }
    }
}
