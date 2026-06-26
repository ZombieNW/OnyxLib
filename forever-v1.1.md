# OnyxLib Design Reference -- v1.1

OnyxLib has not changed, only improved.

## Philosophy

OnyxLib retains its core philosophy: declarative-first configuration that favors composition over restrictive framework abstractions.

v1.1 expands this architecture to support an event-based custom block system utilizing vanilla base blocks overlaid with `ItemDisplay` entities, while keeping the v1.0 custom item system intact.

## Core Concept Additions & Changes

### Items

#### Model Prop

The `.model()` prop specifies a JSON model path within the plugin's asset folder.

```java
ns.item("cool_sword")
    .baseItem(Material.DIAMOND_SWORD)
    .model("items/cool_sword")
    .displayName("Cool Sword");
```

### Blocks

A custom block of a physical, placed vanilla `baseBlock` `Material` and an `ItemDisplay` entity spawned at the block location to provide visuals. Identity is stored via a `PersistentDataContainer` tag on the spawned entity.

```java
ns.block("marble")
    .baseBlock(Material.STONE)
    .displayName("Marble")
    .rotates(false)
    .display(d -> d.allAround("blocks/marble"));
```

#### Required Props
```java
.baseBlock(Material)
```

#### Optional Props
```java
.displayName(String)
.display(Consumer<BlockDisplayBuilder>)
.rotates(boolean)
```

#### Display Builder

OnyxLib automatically generates a block JSON model behind the scenes during resource pack generation.

```java
.display(d -> d
    .allAround("blocks/marble")
)

// or per-face
.display(d -> d
    .north("blocks/marble_front")
    .south("blocks/marble_back")
    .top("blocks/marble_top")
);
```

**Note:** If `.display()` is configured but not all sides are accounted for, an error will be thrown.

### Namespace Queries

To cleanly resolve identity validation, the root namespace acts as the definiative lookup manager.

```java
// Item lookup
OnyxItem item = ns.matchItem(ItemStack stack);

// Block lookup (checks if a tagged display entity exists)
OnyxBlock block = ns.matchBlock(Block block);
```

```java
@EventHandler
public void onPlayerInteract(PlayerInteractEvent event) {
    Player player = event.getPlayer();

    // Custom Item
    ItemStack handItem = player.getInventory().getItemInMainHand();
    OnyxItem customItem = ns.matchItem(handItem);
    if (customItem != null && customItem.getId().equals("ray_gun")) {
        player.sendMessage("Pew pew!");
        return;
    }

    // Custom Block
    if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
        Block clickedBlock = event.getClickedBlock();
        OnyxBlock customBlock = ns.matchBlock(clickedBlock);
        
        if (customBlock != null && customBlock.getId().equals("engine")) {
            player.sendMessage("Engine Started!");
        }
    }
}
```


### Events & Lifecycle

To avoid re-inventing an event system, two new custom Paper events are exposed. It hooks into Paper events at `EventPriority.MONITOR` to safely handle the `ItemDisplay` entities before passing control to the developer-facing event.

- **`OnyxBlockPlaceEvent`:** Fires after a block is successfully placed and its display entity has been spawned and tagged.
- **`OnyxBlockBreakEvent`:** Fires when a block is broken, immediately before the display entity is stripped and deleted by OnyxLib.

```java
@EventHandler
public void onChairPlace(OnyxBlockPlaceEvent event) {
    if (!event.getOnyxBlock().getId().equals("oak_chair")) return;

    Location loc = event.getBlock().getLocation().add(0.5, 0.0, 0.5);
    
    // Spawn an armor stand to handle sitting
    ArmorStand chairSeat = loc.getWorld().spawn(loc, ArmorStand.class, armorStand -> {
        armorStand.setVisible(false);
        armorStand.setGravity(false);
        armorStand.setMarker(true);
        
        // Tag it
        armorStand.getPersistentDataContainer().set(
            new NamespacedKey("my_plugin", "chair_seat"), 
            PersistentDataType.BOOLEAN, 
            true
        );
    });
}

@EventHandler
public void onChairBreak(OnyxBlockBreakEvent event) {
    if (!event.getOnyxBlock().getId().equals("oak_chair")) return;

    // Clean up armor stand
    Location loc = event.getBlock().getLocation().add(0.5, 0.0, 0.5);
    loc.getWorld().getNearbyEntities(loc, 0.5, 0.5, 0.5).stream()
        .filter(e -> e instanceof ArmorStand && e.getPersistentDataContainer().has(new NamespacedKey("my_plugin", "chair_seat")))
        .forEach(Entity::remove);
}
```
