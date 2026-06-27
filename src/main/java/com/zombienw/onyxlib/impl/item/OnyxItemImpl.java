package com.zombienw.onyxlib.impl.item;

import com.zombienw.onyxlib.api.item.OnyxItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.function.Consumer;

/**
 * Implements OnyxItems
 */
public class OnyxItemImpl implements OnyxItem {

    private final String id;
    private final NamespacedKey elementKey; // element specific key
    private final NamespacedKey onyxKey; // unified pdc key

    private Material baseMaterial;
    private Component displayName;
    private String texturePath;
    private String customModelPath;
    private Consumer<ItemMeta> metaConsumer;

    public OnyxItemImpl(String id, NamespacedKey elementKey, NamespacedKey onyxKey) {
        this.id = id;
        this.elementKey = elementKey;
        this.onyxKey = onyxKey;
    }

    public String getId() { return id; }
    public String getTexturePath() { return texturePath; }
    public String getCustomModelPath() { return customModelPath; }
    public Material getBaseMaterial() { return baseMaterial; }
    public NamespacedKey getKey() { return elementKey; }

    @Override
    public OnyxItem baseItem(Material material) {
        this.baseMaterial = material;
        return this;
    }

    @Override
    public OnyxItem displayName(String name) {
        this.displayName = Component.text(name)
                .decoration(TextDecoration.ITALIC, false);
        return this;
    }

    @Override
    public OnyxItem displayName(Component component) {
        this.displayName = component;
        return this;
    }

    @Override
    public OnyxItem texture(String path) {
        // check for dev adding extension
        if (path.endsWith(".png")) {
            throw new IllegalArgumentException(
                    "Texture path for item '" + id + "' must not include the .png extension. " +
                            "Got: \"" + path + "\", expected: \"" + path.substring(0, path.length() - 4) + "\""
            );
        }

        this.texturePath = path;
        return this;
    }

    @Override
    public OnyxItem model(String path) {
        // check for dev adding extension
        if (path.endsWith(".json")) {
            throw new IllegalArgumentException(
                    "Model path for item '" + id + "' must not include the .json extension. " +
                            "Got: \"" + path + "\", expected: \"" + path.substring(0, path.length() - 5) + "\""
            );
        }

        this.customModelPath = path;
        return this;
    }

    @Override
    public OnyxItem itemMeta(Consumer<ItemMeta> metaConsumer) {
        this.metaConsumer = metaConsumer;
        return this;
    }

    @Override
    public ItemStack create() {
        return create(1);
    }

    @Override
    public ItemStack create(int amount) {
        if (this.baseMaterial == null) {
            throw new IllegalStateException("Cannot create OnyxItem '" + id + "' without a defined baseItem().");
        }

        ItemStack stack = new ItemStack(this.baseMaterial, amount);
        ItemMeta meta = stack.getItemMeta();

        if (meta != null) {

            if (this.displayName != null) {
                meta.displayName(this.displayName);
            }

            meta.setItemModel(this.elementKey);
            meta.getPersistentDataContainer().set(this.onyxKey, PersistentDataType.STRING, this.id);

            if (this.metaConsumer != null) {
                this.metaConsumer.accept(meta);
            }

            stack.setItemMeta(meta);
        }

        return stack;
    }
}
