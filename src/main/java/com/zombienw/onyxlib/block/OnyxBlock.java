package com.zombienw.onyxlib.block;

import com.zombienw.onyxlib.core.OnyxNamespace;
import com.zombienw.onyxlib.event.block.*;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import org.bukkit.Material;

/**
 * Descriptor for a block registered inside an {@link OnyxNamespace}.
 *
 * <p>Components:
 * <ul>
 *   <li>A vanilla {@link Material} that is actually placed in the world (the {@code baseBlock}).
 *   <li>A display entity spawned on top of the placed block that renders the custom appearance.
 * </ul>
 *
 * <p>Created via {@link BlockBuilder}. Call {@link BlockBuilder#register()} to commit.
 */
public final class OnyxBlock {

    private final OnyxNamespace namespace;
    private final String id;

    private final Material baseBlock;

    @Nullable
    private final String displayName;

    @Nullable
    private final BlockDisplayConfig displayConfig;

    private final boolean rotates;

    // Hook Events
    @Nullable
    private final Consumer<BlockPlaceEvent> onPlace;

    @Nullable
    private final Consumer<BlockBreakEvent> onBreak;

    @Nullable
    private final Consumer<BlockInteractEvent> onInteract;

    /** Package-private — constructed only by {@link BlockBuilder}. */
    OnyxBlock(
        OnyxNamespace namespace,
        String id,
        Material baseBlock,
        @Nullable String displayName,
        @Nullable BlockDisplayConfig displayConfig,
        boolean rotates,
        @Nullable Consumer<BlockPlaceEvent> onPlace,
        @Nullable Consumer<BlockBreakEvent> onBreak,
        @Nullable Consumer<BlockInteractEvent> onInteract
    ) {
        this.namespace = namespace;
        this.id = id;
        this.baseBlock = baseBlock;
        this.displayName = displayName;
        this.displayConfig = displayConfig;
        this.rotates = rotates;
        this.onPlace = onPlace;
        this.onBreak = onBreak;
        this.onInteract = onInteract;
    }

    // Accessors

    /** @return un-namespaced id, example {@code "marble"} */
    public String id() {
        return id;
    }

    /** @return the owning namespace */
    public OnyxNamespace namespace() {
        return namespace;
    }

    /** @return full key, e.g. {@code "myplugin:marble"} */
    public String key() {
        return namespace.key(id);
    }

    /** @return the vanilla block material placed in the world */
    public Material baseBlock() {
        return baseBlock;
    }

    /** @return display name, or {@code null} if not set */
    @Nullable
    public String displayName() {
        return displayName;
    }

    /** @return the display entity texture config, or {@code null} if no display was configured */
    @Nullable
    public BlockDisplayConfig displayConfig() {
        return displayConfig;
    }

    /**
     * Returns whether the block's display entity rotates to match the player's facing direction
     * when placed.
     *
     * @return {@code true} if the block rotates on placement
     */
    public boolean rotates() {
        return rotates;
    }

    // Hook Events

    /** Fires the {@code onPlace} hook if one is registered. */
    public void firePlace(BlockPlaceEvent event) {
        if (onPlace != null) onPlace.accept(event);
    }

    /** Fires the {@code onBreak} hook if one is registered. */
    public void fireBreak(BlockBreakEvent event) {
        if (onBreak != null) onBreak.accept(event);
    }

    /** Fires the {@code onInteract} hook if one is registered. */
    public void fireInteract(BlockInteractEvent event) {
        if (onInteract != null) onInteract.accept(event);
    }
}
