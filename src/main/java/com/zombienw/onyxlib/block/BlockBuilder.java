package com.zombienw.onyxlib.block;

import com.zombienw.onyxlib.core.OnyxNamespace;
import com.zombienw.onyxlib.event.block.*;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import org.bukkit.Material;

/**
 * Builder for creating and registering a custom {@link OnyxBlock}.
 *
 * <p>Instance via {@link OnyxNamespace#block(String)}, chain prop config calls,
 * then call {@link #register()} to commit the block to the namespace registry.
 *
 * <pre>{@code
 * ns.block("marble")
 *     .baseBlock(Material.STONE)
 *     .displayName("Marble")
 *     .display(d -> d.allAround("blocks/marble.png"))
 *     .register();
 * }</pre>
 *
 * <h3>Required</h3>
 * {@link #baseBlock(Material)} must be called before {@link #register()}.
 */
public final class BlockBuilder {

    private final OnyxNamespace namespace;
    private final String id;

    @Nullable
    private Material baseBlock;

    @Nullable
    private String displayName;

    @Nullable
    private BlockDisplayBuilder displayBuilder;

    private boolean rotates = false;

    @Nullable
    private Consumer<BlockPlaceEvent> onPlace;

    @Nullable
    private Consumer<BlockBreakEvent> onBreak;

    @Nullable
    private Consumer<BlockInteractEvent> onInteract;

    /** Package-private — created by {@link OnyxNamespace#block(String)}. */
    BlockBuilder(OnyxNamespace namespace, String id) {
        this.namespace = namespace;
        this.id = id;
    }

    // Required

    /**
     * Sets the {@link Material} that will be placed in the world to back this block.
     *
     * <p>Choose a material that has the right hardness/sound profile for your block.
     * The display entity will hide the vanilla appearance visually.
     *
     * @param material base block material, must be a placeable block
     * @return this builder
     */
    public BlockBuilder baseBlock(Material material) {
        this.baseBlock = material;
        return this;
    }

    // Display

    /**
     * Sets the display name of the block item
     *
     * @param displayName display name string
     * @return this builder
     */
    public BlockBuilder displayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    /**
     * Configures the display entity that renders on top of the placed block.
     *
     * <p>Receives a fresh {@link BlockDisplayBuilder}, call face methods on it to assign textures.
     *
     * <pre>{@code
     * .display(d -> d.allAround("blocks/marble.png"))
     * // or per-face:
     * .display(d -> d.top("blocks/grass_top.png").allSides("blocks/grass_side.png"))
     * }</pre>
     *
     * @param consumer display builder consumer
     * @return this builder
     */
    public BlockBuilder display(Consumer<BlockDisplayBuilder> consumer) {
        this.displayBuilder = new BlockDisplayBuilder();
        consumer.accept(this.displayBuilder);
        return this;
    }

    /**
     * Sets whether the block's display entity rotates to match the placing player's
     * horizontal facing direction.
     *
     * <p>For directional blocks. Defaults to {@code false}.
     *
     * @param rotates {@code true} to enable rotation on placement
     * @return this builder
     */
    public BlockBuilder rotates(boolean rotates) {
        this.rotates = rotates;
        return this;
    }

    // Hook Events

    /**
     * Called after a player successfully places this block in the world.
     *
     * <p>The display entity has already been spawned by the time this fires.
     *
     * @param handler event handler
     * @return this builder
     */
    public BlockBuilder onPlace(Consumer<BlockPlaceEvent> handler) {
        this.onPlace = handler;
        return this;
    }

    /**
     * Called when a player (or the environment) breaks this block.
     *
     * <p>The display entity is removed before this fires. You can cancel the underlying
     * Bukkit event via {@link BlockBreakEvent#handle()} to prevent the break.
     *
     * @param handler event handler
     * @return this builder
     */
    public BlockBuilder onBreak(Consumer<BlockBreakEvent> handler) {
        this.onBreak = handler;
        return this;
    }

    /**
     * Called when a player right-clicks this block.
     *
     * @param handler event handler
     * @return this builder
     */
    public BlockBuilder onInteract(Consumer<BlockInteractEvent> handler) {
        this.onInteract = handler;
        return this;
    }

    // Register

    /**
     * Validates config, constructs the {@link OnyxBlock}, and registers it in the namespace.
     *
     * @return the registered {@link OnyxBlock}
     * @throws IllegalStateException if {@link #baseBlock(Material)} was not called
     */
    public OnyxBlock register() {
        if (baseBlock == null) {
            throw new IllegalStateException(
                "Block '" +
                    namespace.key(id) +
                    "' requires .baseBlock(Material)."
            );
        }

        BlockDisplayConfig config =
            displayBuilder != null ? displayBuilder.build() : null;

        OnyxBlock block = new OnyxBlock(
            namespace,
            id,
            baseBlock,
            displayName,
            config,
            rotates,
            onPlace,
            onBreak,
            onInteract
        );

        namespace.registry().registerBlock(block);
        return block;
    }
}
