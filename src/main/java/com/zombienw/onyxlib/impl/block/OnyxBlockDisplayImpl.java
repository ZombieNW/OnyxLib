package com.zombienw.onyxlib.impl.block;

import com.zombienw.onyxlib.api.block.OnyxBlockDisplay;
import java.util.HashMap;
import java.util.Map;

/**
 * Implements OnyxBlockDisplays
 */
public class OnyxBlockDisplayImpl implements OnyxBlockDisplay {

    private final Map<Face, String> textures = new HashMap<>();

    // Shortcuts

    @Override
    public OnyxBlockDisplay all(String texturePath) {
        checkExtension(texturePath);
        for (Face face : Face.values()) {
            this.textures.put(face, texturePath);
        }
        return this;
    }

    @Override
    public OnyxBlockDisplay sides(String texturePath) {
        checkExtension(texturePath);
        this.textures.put(Face.NORTH, texturePath);
        this.textures.put(Face.SOUTH, texturePath);
        this.textures.put(Face.EAST, texturePath);
        this.textures.put(Face.WEST, texturePath);
        return this;
    }

    @Override
    public OnyxBlockDisplay vertical(String texturePath) {
        checkExtension(texturePath);
        this.textures.put(Face.TOP, texturePath);
        this.textures.put(Face.BOTTOM, texturePath);
        return this;
    }

    // Individual faces

    @Override
    public OnyxBlockDisplay top(String texturePath) {
        checkExtension(texturePath);
        this.textures.put(Face.TOP, texturePath);
        return this;
    }

    @Override
    public OnyxBlockDisplay bottom(String texturePath) {
        checkExtension(texturePath);
        this.textures.put(Face.BOTTOM, texturePath);
        return this;
    }

    @Override
    public OnyxBlockDisplay north(String texturePath) {
        checkExtension(texturePath);
        this.textures.put(Face.NORTH, texturePath);
        return this;
    }

    @Override
    public OnyxBlockDisplay south(String texturePath) {
        checkExtension(texturePath);
        this.textures.put(Face.SOUTH, texturePath);
        return this;
    }

    @Override
    public OnyxBlockDisplay east(String texturePath) {
        checkExtension(texturePath);
        this.textures.put(Face.EAST, texturePath);
        return this;
    }

    @Override
    public OnyxBlockDisplay west(String texturePath) {
        checkExtension(texturePath);
        this.textures.put(Face.WEST, texturePath);
        return this;
    }

    @Override
    public String getTexture(OnyxBlockDisplay.Face face) {
        return this.textures.get(face);
    }

    private void checkExtension(String path) {
        if (path.endsWith(".png")) {
            throw new IllegalArgumentException(
                    "Texture path for block must not include the .png extension. "
            );
        }
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