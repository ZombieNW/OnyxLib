# OnyxLib Design Reference -- v1.0
How OnyxLib should look, feel, and operate.

---

## Philosophy
OnyxLib is a framework for creating custom content using PaperMC. All custom items map directly to real, pre-existing vanilla objects.

OnyxLib abstracts asset management and overhead so end-developers can focus on what they want to create. However, OnyxLib is not a full abstraction layer, composition and direct-access to native API's is always preferred over restrictive framework "features".

## Core Concepts

---

### Namespaces
Namespaces are the root containers and registries for all custom content. They handle scoping, automatic ID tracking,a nd serve as the source of truth for the resource pack generation pipeline.
```java
OnyxNamespace ns = OnyxLib.namespace("myplugin");
```
Registration becomes immutable once the server plugin is fully enabled.

---

### Items
An item is a declaratively configured wrapper around a vanilla base `Material`.
```java
ns.item("strawberry")
    .displayName("Strawberry")
    .texture("items/strawberry.png")
    .baseItem(Material.APPLE);
```
#### Required Props
```java
.baseItem(Material)
```

#### Abstraction Props
```java
.displayName(String)
.displayName(Component)
.texture(String)
```

#### Escape Hatch
```java
.itemMeta(Consumer<ItemMeta>)
```

#### Item Creation
```java
ItemStack stack = ns.item("strawberry").create();

// Helpers
.create(int amount)
```

#### Examples
```java
// Weapon
ns.item("steel_sword")
    .baseItem(Material.DIAMOND_SWORD)
    .texture("items/steel_sword.png")
    .itemMeta(meta -> {
        AttributeModifier modifier = new AttributeModifier(
            UUID.randomUUID(),
            "generic.attackDamage",
            10.0, // Damage amount
            AttributeModifier.Operation.ADD_NUMBER,
            EquipmentSlot.HAND
        );

        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier);
    });
```

--- 

### Resource Pack Engine

A standout feature of OnyxLib is the automated asset pipeline. Developers do not manually assign `CustomModelData`.
- **Automatic ID Allocation:** When an item is registered, the namespace automatically gives it a unique `CustomModelData` identifier. (ie `namespace:item_name`)
- **State Persistence:** Items are tagged with a `PersistentDataContainer` of its namespace identifier. This ensures items maintain their behavior/identity.
- **Pack Generation:** At the whim of a command, the framework compiles all reigstered item assets, builds the necessary json files, generates the `pack.mcmeta`, and exports a ready-to-use resource pack.

--- 

## Constraints & Omissions
- **No Custom Blocks:** This version of the MVP removes the entire custom block system to reduce scope-creep.
- **No Logic Archetypes:** No rigid or pre-packaged item templates will exist like 'weapon', 'tool', or 'food'. Custom behavior is entirely the responsibility of the developer via event listeners or `itemMeta` attributes.
