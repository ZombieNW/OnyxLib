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

## Lifecycle & Locking

Registration is restricted to the server startup phase. Once the server finishes loading and the `ServerLoadEvent` fires, OnyxLib locks all namespaces.
