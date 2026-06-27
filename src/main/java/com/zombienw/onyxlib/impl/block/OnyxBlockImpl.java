package com.zombienw.onyxlib.impl.block;

import com.zombienw.onyxlib.api.block.OnyxBlock;
import com.zombienw.onyxlib.api.block.OnyxBlockDisplay;
import com.zombienw.onyxlib.api.item.OnyxItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;
import java.util.function.Consumer;

public class OnyxBlockImpl implements OnyxBlock {

    private final String id;
    private final NamespacedKey elementKey;
    private final NamespacedKey onyxKey;

    private Material baseBlock;
    private Component displayName;
    private OnyxBlockDisplay blockDisplay;
    private boolean rotates = false;

    public OnyxBlockImpl(String id, NamespacedKey elementKey, NamespacedKey onyxKey) {
        this.id = id;
        this.elementKey = elementKey;
        this.onyxKey = onyxKey;
    }

    public String getId() { return id; }
    public OnyxBlockDisplay getBlockDisplay() { return blockDisplay; }
    public Material getBaseBlock() { return baseBlock; }
    public NamespacedKey getKey() { return elementKey; }

    @Override
    public OnyxBlock baseBlock(Material material) {
        if (material == null) throw new IllegalArgumentException("Base material cannot be null!");
        if (!material.isBlock()) throw new IllegalArgumentException("Material '" + material.name() + "' is not a placeable block!");
        if (material.isAir()) throw new IllegalArgumentException("Base material cannot be AIR!");

        this.baseBlock = material;
        return this;
    }

    @Override
    public OnyxBlock displayName(String name) {
        this.displayName = Component.text(name)
                .decoration(TextDecoration.ITALIC, false);
        return this;
    }

    @Override
    public OnyxBlock displayName(Component component) {
        this.displayName = component;
        return this;
    }

    @Override
    public OnyxBlock blockDisplay(Consumer<OnyxBlockDisplay> builder) {
        if (builder == null) return this;

        if (this.blockDisplay == null) this.blockDisplay = new OnyxBlockDisplayImpl();

        builder.accept(this.blockDisplay);

        // validate faces
        if (this.blockDisplay instanceof OnyxBlockDisplayImpl impl) {
            Map<String, String> textures = impl.buildTextureMap();
            int expectedFaces = 6;

            if (textures.size() < expectedFaces) {
                throw new IllegalStateException(String.format(
                        "Incomplete display configuration for block '%s'! " +
                                "All %d sides must be accounted for. Found only: %s",
                        this.id, expectedFaces, textures.keySet()
                ));
            }
        }

        return this;
    }

    @Override
    public OnyxBlock rotates(Boolean rotates) {
        this.rotates = rotates != null && rotates;
        return this;
    }

    @Override
    public ItemStack create() {
        return create(1);
    }

    @Override
    public ItemStack create(int amount) {
        if (this.baseBlock == null) {
            throw new IllegalStateException("Cannot create OnyxBlock '" + id + "' without a defined baseBlock().");
        }

        ItemStack item = new ItemStack(this.baseBlock, amount);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            if (this.displayName != null) {
                meta.displayName(this.displayName);
            }

            meta.setItemModel(this.elementKey);
            meta.getPersistentDataContainer().set(this.onyxKey, PersistentDataType.STRING, this.id);

            item.setItemMeta(meta);
        }

        return item;
    }

    @Override
    public Block place(Location location) {
        if (location == null || location.getWorld() == null) throw new IllegalArgumentException("Location and world cannot be null");
        if (this.baseBlock == null) throw new IllegalStateException("Cannot place OnyxBlock '" + id + "' without a defined baseBlock().");

        // Place block
        Block targetBlock = location.getBlock();
        targetBlock.setType(this.baseBlock, false);

//        Sample initial light
//        Block blockAbove = targetBlock.getRelative(org.bukkit.block.BlockFace.UP);
//        int blockLight = blockAbove.getLightFromBlocks();
//        int skyLight = blockAbove.getLightFromSky();

        Location entityLoc = targetBlock.getLocation().add(0.5, 0.5, 0.5);

        if (this.rotates) {
            float yaw = location.getYaw();
            float snappedYaw = Math.round(yaw / 90.0f) * 90.0f;
            entityLoc.setYaw(snappedYaw);
            entityLoc.setPitch(0.0f);
        }

        entityLoc.getWorld().spawn(entityLoc, ItemDisplay.class, display -> {
            ItemStack displayItem = this.create(1);
            display.setItemStack(displayItem);
            display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.FIXED);

            // Matches the listener's exact key structure
            display.getPersistentDataContainer().set(onyxKey, PersistentDataType.STRING, this.id);

            display.setInterpolationDuration(0);
            display.setTeleportDuration(0);

            display.setBrightness(new org.bukkit.entity.Display.Brightness(11, 13));
        });

        return targetBlock;
    }
}
