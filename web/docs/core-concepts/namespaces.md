---
id: namespaces
title: Namespaces
sidebar_position: 2
---

# Namespaces

Namespaces are the root containers/registries for all custom content. They handle scope, id's, and interact with the resource pack pipeline.

## Creating a Namespace

Every OnyxLib plugin must retrieve its unique namespace to register items. Namespaces are generated based on your plugin's name.

```java
import com.zombienw.onyxlib.api.OnyxLib;
import com.zombienw.onyxlib.api.OnyxNamespace;

public class MyPlugin extends JavaPlugin {
    
    @Override
    public void onEnable() {
        OnyxNamespace ns = OnyxLib.namespace(this);
    }
}
```

## Namespace Queries

To resolve custom content identity, the namespace acts as the lookup manager.

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

## Lifecycle & Locking

Registration is restricted to the server startup phase. Once the server finishes loading and the `ServerLoadEvent` fires, OnyxLib locks all namespaces.
