# OnyxLib Design Reference -- v1.0
How OnyxLib should always look and feel.

## Philosophy
OnyxLib is a framework for creating custom content using PaperMC. All items map to real pre-existing objects. Items are backed by `Material`'s, Blocks are backed by placed vanilla blocks with display entities on top.

OnyxLib is meant to be declarative-first. End-developers are focused on "what" they want to create, less of "how" it's created. Though, developers are given full control. This is not a full abstraction layer. The process of composition is preferred over top-level features.

## Core Concepts

### Namespaces
Namespaces are the root containers for all content.
```java
OnyxNamespace ns = OnyxLib.namespace("myplugin");
```
The id decides scope (i.e. `myplugin:strawberry`). The namespace includes things like item and block registration. Registration is immutable after the plugin is enabled. 

### Items
Items are a "configured wrapper" around a base Material/ItemStack.
```java
ns.item("strawberry")
    .displayName("Strawberry")
    .texture("items/strawberry.png")
    .baseItem(Material.APPLE);
```
#### Required Props
```java
.baseItem(Material)
// or
.itemStack(ItemStack)
```

#### Additional Props
```java
.displayName(String)
.texture(String)
.model(String)
.itemMeta(Consumer<ItemMeta>)
```

#### Behavior Hooks
```java
.onUse(event -> {})
.onConsume(event -> {})
.onEquip(event -> {})
.onUnequip(event -> {})
.onHitEntity(event -> {})
```

#### Item Creation
```java
ItemStack stack = ns.item("strawberry").create();
// Helpers
.create(int amount)
.give(Player player)
```

#### Constraints
- No hardcoded "weapon"/"food" archetypes.
- Patterns > Features
- Stats are handled via `itemMeta` and event hooks.

#### Examples
```java
// Food
ns.item("strawberry")
    .baseItem(Material.APPLE)
    .onConsume(e -> {
        // Technical superflous since apple has a hunger amount
        int hungerAmount = 4;
        e.player().setFoodLevel(e.player().getFoodLevel() + hungerAmount);
    });

// Weapon
ns.item("steel_sword")
    .baseItem(Material.DIAMOND_SWORD)
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

### Blocks
A block is:
- A placed vanilla `baseBlock`
- Display entity over the block
```java
ns.block("marble")
    .baseBlock(Material.STONE)
    .display(d -> d.allAround("blocks/marble.png"));
```

#### Required Props
```java
.baseBlock(Material)
.type()
```

#### Optional Props
```java
.displayName(String)
.display(Consumer<BlockDisplayBuilder>)
.rotates(boolean)
```

#### Display Builder
```java
.display(d -> d
    .allAround("blocks/marble.png")
)

// or per-face
.display(d -> d
    .north("...")
    .south("...")
    .top("...")
);
```

#### Behavior Hooks
```java
.onPlace(event -> {})
.onBreak(event -> {})
.onInteract(event -> {})
```

#### Constraints
- No tile entity system
- Interaction is purely event driven
