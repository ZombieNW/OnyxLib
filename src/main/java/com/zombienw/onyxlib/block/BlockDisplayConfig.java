package com.zombienw.onyxlib.block;

import javax.annotation.Nullable;

/**
 * Immutable snapshot of a block's per-face display texture configuration.
 *
 * <p>Used by {@link BlockDisplayBuilder#build()} and stored on {@link OnyxBlock}.
 * This is just data, the actual entity management is in the break/place handlers.
 */
public final class BlockDisplayConfig {

    @Nullable
    private final String north;

    @Nullable
    private final String south;

    @Nullable
    private final String east;

    @Nullable
    private final String west;

    @Nullable
    private final String top;

    @Nullable
    private final String bottom;

    BlockDisplayConfig(
        @Nullable String north,
        @Nullable String south,
        @Nullable String east,
        @Nullable String west,
        @Nullable String top,
        @Nullable String bottom
    ) {
        this.north = north;
        this.south = south;
        this.east = east;
        this.west = west;
        this.top = top;
        this.bottom = bottom;
    }

    /** @return north face texture, or {@code null} */
    @Nullable
    public String north() {
        return north;
    }

    /** @return south face texture, or {@code null} */
    @Nullable
    public String south() {
        return south;
    }

    /** @return east face texture, or {@code null} */
    @Nullable
    public String east() {
        return east;
    }

    /** @return west face texture, or {@code null} */
    @Nullable
    public String west() {
        return west;
    }

    /** @return top face texture, or {@code null} */
    @Nullable
    public String top() {
        return top;
    }

    /** @return bottom face texture, or {@code null} */
    @Nullable
    public String bottom() {
        return bottom;
    }
}
