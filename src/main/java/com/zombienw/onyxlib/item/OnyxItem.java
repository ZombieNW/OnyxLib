package com.zombienw.onyxlib.item;

import com.zombienw.onyxlib.core.OnyxNamespace;
import com.zombienw.onyxlib.event.item.*;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;
import org.jetbrains.annotations.NotNull;

/**
 * Descriptor for an item registered inside an {@link OnyxNamespace}.
 *
 * <p>Items are created via {@link ItemBuilder}, not this class.
 * The builder registers the item into the namespace registry, then it can't be mutated.
 *
 * <p>To create actual {@link ItemStack} instances at runtime, use {@link #create(int)}.
 */
public final class OnyxItem {

    private final OnyxNamespace namespace;
    private final String id;

    // Display
    @Nullable
    private final String displayName;

    @Nullable
    private final String texture;

    @Nullable
    private final String model;

    // Base Item
    @Nullable
    private final Material baseMaterial;

    @Nullable
    private final ItemStack baseItemStack;

    @Nullable
    private final Consumer<org.bukkit.inventory.meta.ItemMeta> itemMetaConsumer;

    // Event Hooks
    @Nullable
    private final Consumer<ItemUseEvent> onUse;

    @Nullable
    private final Consumer<ItemConsumeEvent> onConsume;

    @Nullable
    private final Consumer<ItemEquipEvent> onEquip;

    @Nullable
    private final Consumer<ItemUnequipEvent> onUnequip;

    @Nullable
    private final Consumer<ItemHitEntityEvent> onHitEntity;

    /** Package-private constructed by {@link ItemBuilder}. */
    OnyxItem(
        OnyxNamespace namespace,
        String id,
        @Nullable String displayName,
        @Nullable String texture,
        @Nullable String model,
        @Nullable Material baseMaterial,
        @Nullable ItemStack baseItemStack,
        @Nullable Consumer<org.bukkit.inventory.meta.ItemMeta> itemMetaConsumer,
        @Nullable Consumer<ItemUseEvent> onUse,
        @Nullable Consumer<ItemConsumeEvent> onConsume,
        @Nullable Consumer<ItemEquipEvent> onEquip,
        @Nullable Consumer<ItemUnequipEvent> onUnequip,
        @Nullable Consumer<ItemHitEntityEvent> onHitEntity
    ) {
        this.namespace = namespace;
        this.id = id;
        this.displayName = displayName;
        this.texture = texture;
        this.model = model;
        this.baseMaterial = baseMaterial;
        this.baseItemStack = baseItemStack;
        this.itemMetaConsumer = itemMetaConsumer;
        this.onUse = onUse;
        this.onConsume = onConsume;
        this.onEquip = onEquip;
        this.onUnequip = onUnequip;
        this.onHitEntity = onHitEntity;
    }

    // Item Creation

    /**
     * Creates a new {@link ItemStack} representing this item with a count of 1.
     *
     * @return a new ItemStack
     */
    public ItemStack create() {
        return create(1);
    }

    /**
     * Creates a new {@link ItemStack} representing this item with the given count.
     *
     * @param amount stack size (1–64)
     * @return a new ItemStack
     */
    public ItemStack create(int amount) {
        ItemStack itemStack;

        // override pre-made itemstack
        if (baseItemStack != null) {
            itemStack = baseItemStack().clone();
            itemStack.setAmount(amount);
        } else {
            Material material =
                baseMaterial != null ? baseMaterial : Material.PAPER;
            itemStack = new ItemStack(material, amount);
        }

        ItemMeta meta = itemStack.getItemMeta();
        if (meta == null) return itemStack; // just to cover our ass

        // stamp PDC namespaced key on the item
        org.bukkit.NamespacedKey identityKey = new org.bukkit.NamespacedKey(
            namespace.id(),
            "id"
        );
        meta
            .getPersistentDataContainer()
            .set(
                identityKey,
                org.bukkit.persistence.PersistentDataType.STRING,
                id
            );

        // Display Name
        if (displayName != null) {
            meta.displayName(
                Component.text(displayName).decoration(
                    TextDecoration.ITALIC,
                    false
                )
            );
        }

        // Model Data
        if (texture != null || model != null) {
            CustomModelDataComponent customModelData =
                meta.getCustomModelDataComponent();
            String modelPath = namespace.id() + ":item/" + id;
            customModelData.setStrings(List.of(modelPath));
            meta.setCustomModelDataComponent(customModelData);
        }

        // Extra Meta
        if (itemMetaConsumer != null) {
            itemMetaConsumer.accept(meta);
        }

        itemStack.setItemMeta(meta);
        return itemStack;
    }

    /**
     * Creates a copy of this item and gives it to the given player.
     *
     * <p>Equivalent to {@code player.getInventory().addItem(create())}.
     *
     * @param player receiver
     */
    public void give(Player player) {
        player.getInventory().addItem(create());
    }

    /**
     * Creates {@code amount} copies of this item and gives them to the player.
     *
     * @param player receiver
     * @param amount number of items to give
     */
    public void give(Player player, int amount) {
        player.getInventory().addItem(create(amount));
    }

    // Accessors

    /** @return un-namespaced id, example {@code "strawberry"} */
    public String id() {
        return id;
    }

    /** @return the owning namespace */
    public OnyxNamespace namespace() {
        return namespace;
    }

    /** @return fully qualified key, example {@code "myplugin:strawberry"} */
    public String key() {
        return namespace.key(id);
    }

    /** @return display name, or {@code null} if not set */
    @Nullable
    public String displayName() {
        return displayName;
    }

    /** @return texture path, or {@code null} if not set */
    @Nullable
    public String texture() {
        return texture;
    }

    /** @return model path, or {@code null} if not set */
    @Nullable
    public String model() {
        return model;
    }

    /** @return base material, or {@code null} if an ItemStack was provided instead */
    @Nullable
    public Material baseMaterial() {
        return baseMaterial;
    }

    /** @return base ItemStack, or {@code null} if a Material was provided instead */
    @Nullable
    public ItemStack baseItemStack() {
        return baseItemStack;
    }

    // Hook events

    /** Fires the {@code onUse} hook if one is registered. */
    public void fireUse(ItemUseEvent event) {
        if (onUse != null) onUse.accept(event);
    }

    /** Fires the {@code onConsume} hook if one is registered. */
    public void fireConsume(ItemConsumeEvent event) {
        if (onConsume != null) onConsume.accept(event);
    }

    /** Fires the {@code onEquip} hook if one is registered. */
    public void fireEquip(ItemEquipEvent event) {
        if (onEquip != null) onEquip.accept(event);
    }

    /** Fires the {@code onUnequip} hook if one is registered. */
    public void fireUnequip(ItemUnequipEvent event) {
        if (onUnequip != null) onUnequip.accept(event);
    }

    /** Fires the {@code onHitEntity} hook if one is registered. */
    public void fireHitEntity(ItemHitEntityEvent event) {
        if (onHitEntity != null) onHitEntity.accept(event);
    }
}
