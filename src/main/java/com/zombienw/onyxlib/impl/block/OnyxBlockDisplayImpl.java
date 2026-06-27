package com.zombienw.onyxlib.impl.block;

import com.zombienw.onyxlib.api.block.OnyxBlockDisplay;
import java.util.HashMap;
import java.util.Map;

public class OnyxBlockDisplayImpl implements OnyxBlockDisplay {

    private final Map<Face, String> textures = new HashMap<>();

    // Shortcuts

    @Override
    public OnyxBlockDisplay all(String texturePath) {
        for (Face face : Face.values()) {
            this.textures.put(face, texturePath);
        }
        return this;
    }

    @Override
    public OnyxBlockDisplay sides(String texturePath) {
        this.textures.put(Face.NORTH, texturePath);
        this.textures.put(Face.SOUTH, texturePath);
        this.textures.put(Face.EAST, texturePath);
        this.textures.put(Face.WEST, texturePath);
        return this;
    }

    @Override
    public OnyxBlockDisplay vertical(String texturePath) {
        this.textures.put(Face.TOP, texturePath);
        this.textures.put(Face.BOTTOM, texturePath);
        return this;
    }

    // Individual faces

    @Override
    public OnyxBlockDisplay top(String texturePath) {
        this.textures.put(Face.TOP, texturePath);
        return this;
    }

    @Override
    public OnyxBlockDisplay bottom(String texturePath) {
        this.textures.put(Face.BOTTOM, texturePath);
        return this;
    }

    @Override
    public OnyxBlockDisplay north(String texturePath) {
        this.textures.put(Face.NORTH, texturePath);
        return this;
    }

    @Override
    public OnyxBlockDisplay south(String texturePath) {
        this.textures.put(Face.SOUTH, texturePath);
        return this;
    }

    @Override
    public OnyxBlockDisplay east(String texturePath) {
        this.textures.put(Face.EAST, texturePath);
        return this;
    }

    @Override
    public OnyxBlockDisplay west(String texturePath) {
        this.textures.put(Face.WEST, texturePath);
        return this;
    }

    @Override
    public String getTexture(OnyxBlockDisplay.Face face) {
        return this.textures.get(face);
    }

    // build read only map of textures
    public Map<String, String> buildTextureMap() {
        Map<String, String> namedMap = new HashMap<>();
        for (Map.Entry<Face, String> entry : textures.entrySet()) {
            if (entry.getValue() != null) {
                namedMap.put(entry.getKey().name().toLowerCase(), entry.getValue());
            }
        }
        return namedMap;
    }
}