---
id: items
title: Custom Items
sidebar_position: 3
---

# Custom Items

An OnyxLib item is a wrapper built around a base `Material`.

## Registration

Items are created through your plugin's namespace. `baseItem` is the only strictly required property for an item to function.

```java
OnyxNamespace ns = OnyxLib.namespace(this);

ns.item("strawberry")
    .baseItem(Material.APPLE)
    .displayName("Strawberry")
    .texture("items/strawberry");
```

**Note:** `.texture()` accepts the relative path to the image file inside your plugin's `assets/<namespace>/textures/` directory. **Do not** include the `.png` extension in the string or you will get an error.

## ItemMeta Escape Hatch

OnyxLib is not trying to reinvent the Bukkit API. If you need to modify enchantments, attributes, or persistent data, use the `itemMeta()` consumer. This provides direct access to the underlying `ItemMeta` before the item is built.

```java
ns.item("steel_sword")
    .baseItem(Material.IRON_SWORD)
    .texture("items/steel_sword")
    .itemMeta(meta -> {
        AttributeModifier modifier = new AttributeModifier(
            NamespacedKey.minecraft("attack_damage"),
            10.0,
            AttributeModifier.Operation.ADD_NUMBER,
            EquipmentSlotGroup.HAND
        );

        meta.addAttributeModifier(Attribute.ATTACK_DAMAGE, modifier);
    });
```

## Generating ItemStacks

To use your items, you must get them as `ItemStack` objects. This automatically applies the `CustomModelData` and injects an identifier into the item's `PersistentDataContainer`.

```java
// Create a single item
ItemStack strawberry = ns.item("strawberry").create();

// Create a specific amount
ItemStack strawberryStack = ns.item("strawberry").create(16);
```

## Giving Items

To test items, use the built-in command:
```text
/onyx give <namespace:item_id> [amount]
```
