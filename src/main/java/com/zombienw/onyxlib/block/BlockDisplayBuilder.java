package com.zombienw.onyxlib.block;

import javax.annotation.Nullable;

/**
 * Configures the visual display of a custom block's display entity.
 *
 * <p>Each face can be assigned its own texture path independently, or all six faces can be
 * set at once with {@link #allAround(String)}.
 *
 * <p>Texture paths are relative to the resource pack's
 * {@code assets/<namespace>/textures/} directory.
 *
 * <h3>Usage</h3>
 * <pre>{@code
 * // Uniform texture
 * .display(d -> d.allAround("blocks/marble.png"))
 *
 * // Per-face textures
 * .display(d -> d
 *     .top("blocks/grass_top.png")
 *     .bottom("blocks/dirt.png")
 *     .allSides("blocks/grass_side.png")
 * )
 * }</pre>
 */
public final class BlockDisplayBuilder {

    @Nullable
    private String north;

    @Nullable
    private String south;

    @Nullable
    private String east;

    @Nullable
    private String west;

    @Nullable
    private String top;

    @Nullable
    private String bottom;

    // Helpers

    /**
     * Sets the same texture on all six faces.
     *
     * @param texturePath texture path
     * @return this builder
     */
    public BlockDisplayBuilder allAround(String texturePath) {
        this.north = this.south = this.east = this.west = this.top =
            this.bottom = texturePath;
        return this;
    }

    /**
     * Sets the same texture on the four horizontal sides (north, south, east, west),
     * leaving top and bottom unchanged.
     *
     * @param texturePath texture path
     * @return this builder
     */
    public BlockDisplayBuilder allSides(String texturePath) {
        this.north = this.south = this.east = this.west = texturePath;
        return this;
    }

    // Per-face

    /**
     * Sets the north-face texture.
     *
     * @param texturePath texture path
     * @return this builder
     */
    public BlockDisplayBuilder north(String texturePath) {
        this.north = texturePath;
        return this;
    }

    /**
     * Sets the south-face texture.
     *
     * @param texturePath texture path
     * @return this builder
     */
    public BlockDisplayBuilder south(String texturePath) {
        this.south = texturePath;
        return this;
    }

    /**
     * Sets the east-face texture.
     *
     * @param texturePath texture path
     * @return this builder
     */
    public BlockDisplayBuilder east(String texturePath) {
        this.east = texturePath;
        return this;
    }

    /**
     * Sets the west-face texture.
     *
     * @param texturePath texture path
     * @return this builder
     */
    public BlockDisplayBuilder west(String texturePath) {
        this.west = texturePath;
        return this;
    }

    /**
     * Sets the top-face texture.
     *
     * @param texturePath texture path
     * @return this builder
     */
    public BlockDisplayBuilder top(String texturePath) {
        this.top = texturePath;
        return this;
    }

    /**
     * Sets the bottom-face texture.
     *
     * @param texturePath texture path
     * @return this builder
     */
    public BlockDisplayBuilder bottom(String texturePath) {
        this.bottom = texturePath;
        return this;
    }

    // Accessors

    /** @return north face texture path, or {@code null} */
    @Nullable
    public String north() {
        return north;
    }

    /** @return south face texture path, or {@code null} */
    @Nullable
    public String south() {
        return south;
    }

    /** @return east face texture path, or {@code null} */
    @Nullable
    public String east() {
        return east;
    }

    /** @return west face texture path, or {@code null} */
    @Nullable
    public String west() {
        return west;
    }

    /** @return top face texture path, or {@code null} */
    @Nullable
    public String top() {
        return top;
    }

    /** @return bottom face texture path, or {@code null} */
    @Nullable
    public String bottom() {
        return bottom;
    }

    /**
     * Returns {@code true} if at least one face has a texture assigned.
     *
     * @return {@code true} if any face texture is set
     */
    public boolean hasAnyTexture() {
        return (
            north != null ||
            south != null ||
            east != null ||
            west != null ||
            top != null ||
            bottom != null
        );
    }

    /**
     * Builds a snapshot of this display configuration.
     *
     * <p>Package-private — called by {@link BlockBuilder} at registration time.
     *
     * @return immutable snapshot
     */
    BlockDisplayConfig build() {
        return new BlockDisplayConfig(north, south, east, west, top, bottom);
    }
}
