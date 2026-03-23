package com.zombienw.onyxLib.items;

public class ItemAsset {

    public static final String DEFAULT_MODEL_PARENT = "item/generated";

    private final String texturePath;
    private final String modelParent;
    private final int cmdOverride;

    private ItemAsset(String texturePath, String modelParent, int cmdOverride) {
        this.texturePath = texturePath;
        this.modelParent = modelParent;
        this.cmdOverride = cmdOverride;
    }

    public String getTexturePath() { return texturePath; }
    public String getModelParent() { return modelParent; }
    public int getCmdOverride() { return cmdOverride; }
    public boolean hasCmdOverride() { return cmdOverride != 0; }

    public static Builder builder(String texturePath) {
        return new Builder(texturePath);
    }

    public static class Builder {

        private final String texturePath;
        private String modelParent = DEFAULT_MODEL_PARENT;
        private int cmdOverride = 0;

        public Builder(String texturePath) {
            this.texturePath = texturePath;
        }

        public Builder modelParent(String modelParent) {
            this.modelParent = modelParent;
            return this;
        }

        public Builder cmdOverride(int value) {
            if (value < 1) throw new IllegalArgumentException("CMD override must be >= 1");
            this.cmdOverride = value;
            return this;
        }

        public ItemAsset build() {
            return new ItemAsset(texturePath, modelParent, cmdOverride);
        }
    }
}
