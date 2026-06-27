package com.zombienw.onyxlib.api.block;

/**
 * A builder interface used to map textures to the faces of a custom block.
 * Texture conflicts are resolved by execution order.
 */
public interface OnyxBlockDisplay {

    /**
     * Represents the six faces of a standard block.
     */
    enum Face {
        TOP, BOTTOM, NORTH, SOUTH, EAST, WEST
    }

    /**
     * Gets the current texture path assigned to a specific face.
     * @param face The block face to query.
     * @return The string path of the texture, or {@code null} if not found.
     */
    String getTexture(Face face);

    /**
     * Maps a single texture to all six faces of the block.
     *
     * @param texturePath The path to the texture asset.
     * @return This OnyxBlockDisplay instance.
     */
    OnyxBlockDisplay all(String texturePath);

    /**
     * Maps a texture to the four lateral sides of the block (North, East, South, and West).
     * This does not modify the Top or Bottom faces.
     *
     * @param texturePath The path to the texture asset.
     * @return This OnyxBlockDisplay instance.
     */
    OnyxBlockDisplay sides(String texturePath);

    /**
     * Maps a texture to both the Top and Bottom faces of the block.
     * This does not modify the lateral side faces.
     *
     * @param texturePath The path to the texture asset.
     * @return This OnyxBlockDisplay instance.
     */
    OnyxBlockDisplay vertical(String texturePath);

    /**
     * Maps a texture specifically to the top face of the block.
     *
     * @param texturePath The path to the texture asset.
     * @return This OnyxBlockDisplay instance.
     */
    OnyxBlockDisplay top(String texturePath);

    /**
     * Maps a texture specifically to the bottom face of the block.
     *
     * @param texturePath The path to the texture asset.
     * @return This OnyxBlockDisplay instance.
     */
    OnyxBlockDisplay bottom(String texturePath);

    /**
     * Maps a texture specifically to the north face of the block.
     *
     * @param texturePath The path to the texture asset.
     * @return This OnyxBlockDisplay instance.
     */
    OnyxBlockDisplay north(String texturePath);

    /**
     * Maps a texture specifically to the east face of the block.
     *
     * @param texturePath The path to the texture asset.
     * @return This OnyxBlockDisplay instance.
     */
    OnyxBlockDisplay east(String texturePath);

    /**
     * Maps a texture specifically to the south face of the block.
     *
     * @param texturePath The path to the texture asset.
     * @return This OnyxBlockDisplay instance.
     */
    OnyxBlockDisplay south(String texturePath);

    /**
     * Maps a texture specifically to the west face of the block.
     *
     * @param texturePath The path to the texture asset.
     * @return This OnyxBlockDisplay instance.
     */
    OnyxBlockDisplay west(String texturePath);
}
