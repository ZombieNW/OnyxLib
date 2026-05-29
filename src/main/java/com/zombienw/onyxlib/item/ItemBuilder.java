package com.zombienw.onyxlib.item;

import com.zombienw.onyxlib.core.OnyxNamespace;
import com.zombienw.onyxlib.event.item.*;
import java.util.function.Consumer;
import javax.annotation.Nullable;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Builder for creating and registering an {@link OnyxItem}.
 *
 * <p>Instance via {@link OnyxNamespace#item(String)}, chain prop config calls,
 * then call {@link #register()} to commit the item to the namespace registry.
 *
 * <pre>{@code
 * ns.item("strawberry")
 *     .baseItem(Material.APPLE)
 *     .displayName("Strawberry")
 *     .texture("items/strawberry.png")
 *     .onConsume(e -> e.player().setFoodLevel(e.player().getFoodLevel() + 4))
 *     .register();
 * }</pre>
 *
 * <h3>Required</h3>
 * Either {@link #baseItem(Material)} or {@link #itemStack(ItemStack)} must be called
 * before {@link #register()}.
 */
public final class ItemBuilder {

    private final OnyxNamespace namespace;
    private final String id;

    // Display
    @Nullable
    private String displayName;

    @Nullable
    private String texture;

    @Nullable
    private String model;

    // Base Item
    @Nullable
    private Material baseMaterial;

    @Nullable
    private ItemStack baseItemStack;

    @Nullable
    private Consumer<ItemMeta> itemMetaConsumer;

    // Hook Events
    @Nullable
    private Consumer<ItemUseEvent> onUse;

    @Nullable
    private Consumer<ItemConsumeEvent> onConsume;

    @Nullable
    private Consumer<ItemEquipEvent> onEquip;

    @Nullable
    private Consumer<ItemUnequipEvent> onUnequip;

    @Nullable
    private Consumer<ItemHitEntityEvent> onHitEntity;

    /** Package-private — created by {@link OnyxNamespace#item(String)}. */
    public ItemBuilder(OnyxNamespace namespace, String id) {
        this.namespace = namespace;
        this.id = id;
    }

    // Base Item

    /**
     * Sets the base {@link Material} for the item.
     *
     * <p>This is the vanilla item type the custom item is built on top of. Mutually exclusive
     * with {@link #itemStack(ItemStack)}.
     *
     * @param material base material
     * @return this builder
     */
    public ItemBuilder baseItem(Material material) {
        this.baseMaterial = material;
        this.baseItemStack = null;
        return this;
    }

    /**
     * Sets a pre-made {@link ItemStack} as the base of this custom item.
     *
     * <p>Useful when you need fine-grained control over the starting stack. Mutually exclusive
     * with {@link #baseItem(Material)}.
     *
     * @param stack base item stack — will be cloned internally
     * @return this builder
     */
    public ItemBuilder itemStack(ItemStack stack) {
        this.baseItemStack = stack.clone();
        this.baseMaterial = null;
        return this;
    }

    // Display

    /**
     * Sets the display name shown to players.
     *
     * @param displayName display name string
     * @return this builder
     */
    public ItemBuilder displayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    /**
     * Sets the resource-pack texture path for this item's custom model.
     *
     * <p>The path should be relative to the plugin's {@code assets/<namespace>/textures/}
     * directory, example {@code "items/strawberry.png"}.
     *
     * @param texturePath texture path
     * @return this builder
     */
    public ItemBuilder texture(String texturePath) {
        this.texture = texturePath;
        return this;
    }

    /**
     * Sets the resource-pack model path for this item, overriding {@link #texture(String)}.
     *
     * <p>Use this when you need a full custom model rather than a simple texture override.
     * Path is relative to {@code assets/<namespace>/models/}.
     *
     * @param modelPath model path
     * @return this builder
     */
    public ItemBuilder model(String modelPath) {
        this.model = modelPath;
        return this;
    }

    /**
     * Provides a callback to mutate the item's {@link ItemMeta} before it is finalised.
     *
     * <p>Use to add any meta not covered by the builder; attribute modifiers,
     * enchantments, lore, flags, etc.
     *
     * <pre>{@code
     * .itemMeta(meta -> {
     *     meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier);
     * })
     * }</pre>
     *
     * @param consumer meta consumer
     * @return this builder
     */
    public ItemBuilder itemMeta(Consumer<ItemMeta> consumer) {
        this.itemMetaConsumer = consumer;
        return this;
    }

    // Hook Events

    /**
     * Called when a player right-clicks with this item.
     *
     * @param handler event handler
     * @return this builder
     */
    public ItemBuilder onUse(Consumer<ItemUseEvent> handler) {
        this.onUse = handler;
        return this;
    }

    /**
     * Called when a player finishes consuming this item (food/potion).
     *
     * @param handler event handler
     * @return this builder
     */
    public ItemBuilder onConsume(Consumer<ItemConsumeEvent> handler) {
        this.onConsume = handler;
        return this;
    }

    /**
     * Called when a player equips this item (armour or off-hand swap).
     *
     * @param handler event handler
     * @return this builder
     */
    public ItemBuilder onEquip(Consumer<ItemEquipEvent> handler) {
        this.onEquip = handler;
        return this;
    }

    /**
     * Called when a player un-equips this item.
     *
     * @param handler event handler
     * @return this builder
     */
    public ItemBuilder onUnequip(Consumer<ItemUnequipEvent> handler) {
        this.onUnequip = handler;
        return this;
    }

    /**
     * Called when a player hits an entity while holding this item.
     *
     * @param handler event handler
     * @return this builder
     */
    public ItemBuilder onHitEntity(Consumer<ItemHitEntityEvent> handler) {
        this.onHitEntity = handler;
        return this;
    }

    // Register Terminal

    /**
     * Validates config, constructs the {@link OnyxItem}, and registers it in the namespace.
     *
     * @return the registered {@link OnyxItem}
     * @throws IllegalStateException if neither {@link #baseItem(Material)} nor {@link #itemStack(ItemStack)} was called
     */
    public OnyxItem register() {
        if (baseMaterial == null && baseItemStack == null) {
            throw new IllegalStateException(
                "Item '" +
                    namespace.key(id) +
                    "' requires either .baseItem(Material) or .itemStack(ItemStack)."
            );
        }

        OnyxItem item = new OnyxItem(
            namespace,
            id,
            displayName,
            texture,
            model,
            baseMaterial,
            baseItemStack,
            itemMetaConsumer,
            onUse,
            onConsume,
            onEquip,
            onUnequip,
            onHitEntity
        );

        namespace.registry().registerItem(item);
        return item;
    }
}
