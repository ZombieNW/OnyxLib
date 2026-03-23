package com.zombienw.onyxLib.items;

public class ItemAsset {

    public static final String DEFAULT_MODEL_PARENT = "item/generated";

    private final String texturePath;
    private final String modelParent;

    private ItemAsset(String texturePath, String modelParent) {
        this.texturePath = texturePath;
        this.modelParent = modelParent;
    }

    public String getTexturePath() { return texturePath; }
    public String getModelParent() { return modelParent; }

    public static Builder builder(String texturePath) {
        return new Builder(texturePath);
    }

    public static class Builder {

        private final String texturePath;
        private String modelParent = DEFAULT_MODEL_PARENT;

        public Builder(String texturePath) {
            this.texturePath = texturePath;
        }

        public Builder modelParent(String modelParent) {
            this.modelParent = modelParent;
            return this;
        }

        public ItemAsset build() {
            return new ItemAsset(texturePath, modelParent);
        }
    }
}
