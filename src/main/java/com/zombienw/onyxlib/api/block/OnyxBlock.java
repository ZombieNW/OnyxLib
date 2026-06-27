package com.zombienw.onyxlib.api.block;

import com.zombienw.onyxlib.api.OnyxElement;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public interface OnyxBlock extends OnyxElement {

    /**
     * Defines the underlying vanilla Minecraft block underneath this custom block.
     * @param material The vanilla block Material to use (must be a placeable block)
     * @return This OnyxBlock instance.
     * @throws IllegalArgumentException If provided Material is not a valid block.
     */
    OnyxBlock baseBlock(Material material);

    /**
     * Sets the display name using a legacy string with no italics.
     * @param name The display name.
     * @return This OnyxBlock instance.
     */
    OnyxBlock displayName(String name);

    /**
     * Sets the display name using an Adventure Component.
     * @param component The display name component.
     * @return This OnyxBlock instance.
     */
    OnyxBlock displayName(Component component);

    /**
     * Configures the visual faces of the custom block using a block display builder.
     * <b>Note:</b> If this method is invoked, all relevant faces of the block must
     * be explicitly defined, otherwise an error will be thrown during resource pack generation.
     * @param builder A consumer managing the texture mappings for the block's faces.
     * @return This OnyxBlock instance.
     */
    OnyxBlock blockDisplay(Consumer<OnyxBlockDisplay> builder);

    /**
     * Determines if the custom block should rotate to face the player upon placement.
     * @param rotates True if the custom block should face the player when placed, false otherwise.
     * @return This OnyxBlock instance.
     */
    OnyxBlock rotates(Boolean rotates);

    /**
     * Places an OnyxBlock at a given location.
     * @param location The location of the OnyxBlock to be placed.
     * @return The Block after placement.
     */
    Block place(Location location);
}
