---
id: blocks
title: Custom Blocks
sidebar_position: 4
---

# Custom Blocks

An OnyxLib `OnyxBlock` is a wrapper built on a "real" `Material` block and an `ItemDisplay` display entity complete with dynamic lighting updating and rotation logic.

## Registration

Registering an `OnyxBlock` works similarly to items.

```java
OnyxNamespace ns = OnyxLib.namespace(this);

ns.block("chair")
    .baseBlock(Material.OAK_STAIRS)
    .displayName("Oak Chair")
    .rotates(true) // Faces the player when placed
    .model("blocks/chair"); // Loads assets/myplugin/models/blocks/chair.json
```

## Block Model Generation

Using the `.blockDisplay()` builder, you can let OnyxLib generate a default block model for you!

```java
ns.block("marble")
    .baseBlock(Material.STONE)
    .displayName("Marble")
    .blockDisplay(display -> display
        .all("blocks/marble")
    );
```

#### blockDisplay Functions
```java
    // All six faces
    OnyxBlockDisplay all(String);

    // North, East, South, & West
    OnyxBlockDisplay sides(String);

    // Top & Bottom
    OnyxBlockDisplay vertical(String);

    // Individual Sides
    OnyxBlockDisplay top(String);
    OnyxBlockDisplay bottom(String);
    OnyxBlockDisplay north(String);
    OnyxBlockDisplay east(String);
    OnyxBlockDisplay south(String);
    OnyxBlockDisplay west(String);
```

**Note:** All six block faces must be assigned a texture, or the asset validator will throw an error.

## Placing & Breaking

Custom blocks are placed in the world via normal player interactions or programmatically using the `.place(Location)` method.

OnyxLib handles the lifecycle automatically, tracking physical blocks and their `ItemDisplay` counterparts. Two new Bukkit events are exposed to you for your programming pleasure.

* `OnyxBlockBreakEvent(Block, Player, OnyxBlock)`
* `OnyxBlockPlaceEvent(Block, Player, OnyxBlock)`
