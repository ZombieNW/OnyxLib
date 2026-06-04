package com.zombienw.onyxlib.impl.item;

import com.zombienw.onyxlib.api.item.OnyxItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.function.Consumer;

public class OnyxItemImpl implements OnyxItem {

    private final String id;
    private final NamespacedKey pdcKey;
    private final NamespacedKey itemKey;

    private Material baseMaterial;
    private Component displayName;
    private String texturePath;
    private Consumer<ItemMeta> metaConsumer;

    public OnyxItemImpl(String id, NamespacedKey itemKey, NamespacedKey pdcKey) {
        this.id = id;
        this.itemKey = itemKey;
        this.pdcKey = pdcKey;
    }

    public String getId() { return id; }
    public String getTexturePath() { return texturePath; }
    public Material getBaseMaterial() { return baseMaterial; }
    public NamespacedKey getKey() { return itemKey; }

    @Override
    public OnyxItem baseItem(Material material) {
        this.baseMaterial = material;
        return this;
    }

    @Override
    public OnyxItem displayName(String name) {
        this.displayName = Component.text(name);
        return this;
    }

    @Override
    public OnyxItem displayName(Component component) {
        this.displayName = component;
        return this;
    }

    @Override
    public OnyxItem texture(String path) {
        this.texturePath = path;
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

            meta.setItemModel(this.itemKey);
            meta.getPersistentDataContainer().set(this.pdcKey, PersistentDataType.STRING, this.id);

            if (this.metaConsumer != null) {
                this.metaConsumer.accept(meta);
            }

            stack.setItemMeta(meta);
        }

        return stack;
    }
}
